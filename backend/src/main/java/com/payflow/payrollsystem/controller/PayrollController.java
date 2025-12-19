package com.payflow.payrollsystem.controller;

import com.payflow.payrollsystem.model.Employee;
import com.payflow.payrollsystem.model.Payslip;
import com.payflow.payrollsystem.model.Salary;
import com.payflow.payrollsystem.service.PayrollService;
import com.payflow.payrollsystem.service.PayslipPdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    private final PayrollService payrollService;
    private final PayslipPdfService payslipPdfService;

    public PayrollController(PayrollService payrollService, PayslipPdfService payslipPdfService) {
        this.payrollService = payrollService;
        this.payslipPdfService = payslipPdfService;
    }

    @GetMapping("/employees/{companyId}")
    @PreAuthorize("hasAnyRole('HR','FINANCE')")
    public ResponseEntity<List<Employee>> getEmployees(@PathVariable Long companyId) {
        return ResponseEntity.ok(payrollService.getEmployeesByCompany(companyId));
    }

    @GetMapping("/employees")
    @PreAuthorize("hasAnyRole('HR','FINANCE')")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(payrollService.getAllEmployees());
    }

    @PostMapping("/employees")
    @PreAuthorize("hasAnyRole('HR','FINANCE')")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(payrollService.addEmployee(employee));
    }

    @PostMapping("/salaries")
    @PreAuthorize("hasAnyRole('HR','FINANCE')")
    public ResponseEntity<Salary> addSalary(@RequestBody Salary salary) {
        return ResponseEntity.ok(payrollService.addSalary(salary));
    }

    @PostMapping("/payslips/{employeeId}")
    @PreAuthorize("hasAnyRole('HR','FINANCE')")
    public ResponseEntity<Payslip> generatePayslip(@PathVariable Long employeeId) {
        return ResponseEntity.ok(payrollService.generatePayslip(employeeId));
    }

    @GetMapping("/payslips/{payslipId}/pdf")
    @PreAuthorize("hasAnyRole('HR','FINANCE','EMPLOYEE')")
    public ResponseEntity<byte[]> downloadPayslipPdf(@PathVariable Long payslipId) {
        byte[] pdf = payslipPdfService.generatePayslipPdf(payslipId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "payslip.pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}