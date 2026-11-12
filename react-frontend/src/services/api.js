import axios from 'axios'
const api = axios.create({ baseURL: 'http://localhost:8080/api' })
export const getDrifts = () => api.get('/drifts')
export const runScan = () => api.post('/drifts/scan')
export const getJournal = () => api.get('/journal')
export const decideApproval = (id, body) => api.post(`/approvals/${id}`, body)
export const createPlan = id => api.post(`/remediations/plan/${id}`)
export const executePlan = id => api.post(`/remediations/${id}/execute`)
export default api
