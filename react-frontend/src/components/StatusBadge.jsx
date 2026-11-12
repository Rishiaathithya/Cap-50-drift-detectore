import React from 'react'
export default function StatusBadge({value}){const v=(value||'UNKNOWN').toLowerCase();return <span className={`badge ${v}`}>{value||'UNKNOWN'}</span>}
