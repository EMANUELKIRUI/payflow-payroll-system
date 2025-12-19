package com.payflow.payrollsystem.service;

import com.payflow.payrollsystem.model.*;
import com.payflow.payrollsystem.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PayrollService {

    private final EmployeeRepository employeeRepository;
    private final SalaryRepository salaryRepository;
    private final TaxRepository taxRepository;
    private final PayslipRepository payslipRepository;
    private final AuditLogRepository auditLogRepository;

    public PayrollService(EmployeeRepository employeeRepository, SalaryRepository salaryRepository,
                          TaxRepository taxRepository, PayslipRepository payslipRepository,
                          AuditLogRepository auditLogRepository) {
        this.employeeRepository = employeeRepository;
        this.salaryRepository = salaryRepository;
        this.taxRepository = taxRepository;
        this.payslipRepository = payslipRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public List<Employee> getEmployeesByCompany(Long companyId) {
        return employeeRepository.findByCompanyId(companyId);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee addEmployee(Employee employee) {
        Employee saved = employeeRepository.save(employee);
        logAudit("Employee created: " + employee.getFirstName() + " " + employee.getLastName());
        return saved;
    }

    public Salary addSalary(Salary salary) {
        return salaryRepository.save(salary);
    }

    public Tax calculateTax(Salary salary) {
        BigDecimal gross = salary.getBasicSalary().add(salary.getAllowances() != null ? salary.getAllowances() : BigDecimal.ZERO);
        BigDecimal taxAmount = calculateProgressiveTax(gross);
        Tax tax = new Tax();
        tax.setSalary(salary);
        tax.setTaxAmount(taxAmount);
        tax.setCreatedAt(LocalDateTime.now());
        return taxRepository.save(tax);
    }

    private BigDecimal calculateProgressiveTax(BigDecimal gross) {
        // Progressive tax calculation (annual brackets, simplified example)
        // Assumes monthly gross is provided; for accuracy, annualize if needed
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal remaining = gross;

        // Example progressive tax brackets (annual, simplified)
        // 0 - 10,000: 0%
        if (remaining.compareTo(BigDecimal.valueOf(10000)) > 0) {
            remaining = remaining.subtract(BigDecimal.valueOf(10000));
        } else {
            return tax;
        }

        // 10,000 - 20,000: 10%
        BigDecimal bracket2 = BigDecimal.valueOf(10000);
        if (remaining.compareTo(bracket2) > 0) {
            tax = tax.add(bracket2.multiply(BigDecimal.valueOf(0.10)));
            remaining = remaining.subtract(bracket2);
        } else {
            tax = tax.add(remaining.multiply(BigDecimal.valueOf(0.10)));
            return tax;
        }

        // 20,000 - 50,000: 20%
        BigDecimal bracket3 = BigDecimal.valueOf(30000);
        if (remaining.compareTo(bracket3) > 0) {
            tax = tax.add(bracket3.multiply(BigDecimal.valueOf(0.20)));
            remaining = remaining.subtract(bracket3);
        } else {
            tax = tax.add(remaining.multiply(BigDecimal.valueOf(0.20)));
            return tax;
        }

        // Above 50,000: 30%
        tax = tax.add(remaining.multiply(BigDecimal.valueOf(0.30)));
        return tax;
    }

    private void logAudit(String action) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        // Assume userRepository is injected, but for simplicity, create log without user
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }

    public Payslip generatePayslip(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        // Assume latest salary
        Salary salary = employee.getSalaries().get(employee.getSalaries().size() - 1);
        BigDecimal gross = salary.getBasicSalary().add(salary.getAllowances() != null ? salary.getAllowances() : BigDecimal.ZERO);

        // Calculate tax
        Tax tax = calculateTax(salary);

        // Calculate other deductions
        BigDecimal socialSecurity = gross.multiply(BigDecimal.valueOf(0.062)); // 6.2%
        BigDecimal medicare = gross.multiply(BigDecimal.valueOf(0.0145)); // 1.45%
        BigDecimal totalDeductions = tax.getTaxAmount().add(socialSecurity).add(medicare);

        BigDecimal netPay = gross.subtract(totalDeductions);

        Payslip payslip = new Payslip();
        payslip.setEmployee(employee);
        payslip.setNetPay(netPay);
        payslip.setGeneratedAt(LocalDateTime.now());
        Payslip saved = payslipRepository.save(payslip);
        logAudit("Payslip generated for employee: " + employee.getFirstName() + " " + employee.getLastName());
        return saved;
    }
}