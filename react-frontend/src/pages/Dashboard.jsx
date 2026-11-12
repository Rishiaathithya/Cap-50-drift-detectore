import React,{useEffect,useState} from 'react'
import {getDrifts,runScan,createPlan,decideApproval,executePlan} from '../services/api'
import DriftCard from '../components/DriftCard'
export default function Dashboard(){
 const [drifts,setDrifts]=useState([]),[message,setMessage]=useState(''),[busy,setBusy]=useState(false)
 const refresh=()=>getDrifts().then(r=>setDrifts(r.data)).catch(()=>setMessage('Cannot reach backend. Start Spring Boot and MySQL.'))
 useEffect(()=>{refresh()},[])
 const scan=async()=>{setBusy(true);setMessage('Scanning...');try{const r=await runScan();setMessage(`Scan completed (${r.data.mode}). ${r.data.findings?.length||0} finding(s) detected.`);refresh()}catch(e){setMessage(e.response?.data?.message||'Scan failed. Check Python service and backend logs.')}finally{setBusy(false)}}
 const plan=async id=>{try{const r=await createPlan(id);setMessage(`Plan ${r.data.id} created. Review before approval.`)}catch(e){setMessage('Could not create remediation plan.')}}
 const decide=decision=>async id=>{const comments=window.prompt(`Comments for ${decision.toLowerCase()}:` )||'';try{await decideApproval(id,{decision,approvedBy:'demo-admin',comments});setMessage(`Drift #${id}: ${decision}`);refresh()}catch(e){setMessage('Approval failed.')}}
 const execute=async id=>{try{const p=await createPlan(id);const r=await executePlan(p.data.id);setMessage(r.data.resultMessage||r.data.status)}catch(e){setMessage(e.response?.data?.message||'Execution blocked. Ensure an approved remediation plan exists.')}} 
 const unauthorized=drifts.filter(d=>d.classification==='UNAUTHORIZED').length
 const pending=drifts.filter(d=>d.status==='OPEN').length
 return <div><div className="page-head"><div><p className="eyebrow">OVERVIEW</p><h1>Infrastructure health</h1><p className="muted">Compare deployed AWS resources against your approved baseline.</p></div><button className="primary" disabled={busy} onClick={scan}>{busy?'Scanning...':'▶ Run scan'}</button></div>
 {message&&<div className="notice">{message}</div>}
 <div className="stats"><div className="stat"><span>Drift findings</span><strong>{drifts.length}</strong></div><div className="stat"><span>Unauthorized</span><strong className="red">{unauthorized}</strong></div><div className="stat"><span>Awaiting review</span><strong>{pending}</strong></div><div className="stat"><span>Monitored services</span><strong>2</strong></div></div>
 <div className="section-head"><div><h2>Detected drift</h2><p className="muted">Review every difference before taking action.</p></div><button onClick={refresh}>Refresh</button></div>
 <div className="drift-list">{drifts.length?drifts.map(d=><DriftCard key={d.id} drift={d} onPlan={plan} onApprove={decide('APPROVED')} onReject={decide('REJECTED')} onExecute={execute}/>):<div className="empty">No findings yet. Run a scan to compare infrastructure with the baseline.</div>}</div>
 </div>
}
