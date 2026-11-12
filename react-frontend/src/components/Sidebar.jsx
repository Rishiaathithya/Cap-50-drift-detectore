import React from 'react'
import { NavLink } from 'react-router-dom'
export default function Sidebar(){return <aside className="sidebar">
  <p className="side-label">WORKSPACE</p>
  <NavLink to="/">▦ Dashboard</NavLink><NavLink to="/infrastructure">☁ Infrastructure</NavLink>
  <NavLink to="/approvals">✓ Approvals</NavLink><NavLink to="/journal">◷ Change journal</NavLink>
</aside>}
