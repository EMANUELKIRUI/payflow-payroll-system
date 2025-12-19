
import React from 'react';
import { Link } from 'react-router-dom';

export default function Dashboard() {
  return (
    <div>
      <h1>Dashboard</h1>
      <p>Company overview & stats</p>
      <Link to="/payroll">Go to Payroll</Link>
    </div>
  );
}
