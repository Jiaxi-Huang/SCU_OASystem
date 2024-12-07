export interface Reimbursement {
    reimbursement_id: string
    user_id: string
    amount: number
    description: string
    status: string  // 确保包含 status 字段
    submitted_at: string
}
