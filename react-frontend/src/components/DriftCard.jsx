import React from 'react'
import StatusBadge from './StatusBadge'
export default function DriftCard({drift,onPlan,onApprove,onReject,onExecute}){
 return <article className="drift-card">
  <div className="card-top"><div><small>{drift.resourceType} · {drift.resourceId}</small><h3>{drift.attributeName}</h3></div><StatusBadge value={drift.severity}/></div>
  <div className="comparison"><div><small>EXPECTED</small><code>{drift.expectedValue}</code></div><div><small>OBSERVED</small><code>{drift.actualValue}</code></div></div>
  <div className="card-bottom"><StatusBadge value={drift.classification}/><StatusBadge value={drift.status}/></div>
  <div className="actions">
   <button onClick={()=>onPlan(drift.id)}>Generate plan</button>
   <button className="approve" onClick={()=>onApprove(drift.id)}>Approve</button>
   <button className="reject" onClick={()=>onReject(drift.id)}>Reject</button>
   {drift.status==='APPROVED' && <button className="execute" onClick={()=>onExecute(drift.id)}>Execute demo</button>}
  </div>
 </article>
}
