export interface LeaveApproval {
    leave_id: number;
    user_id: number;
    start_date: string;
    end_date: string;
    reason: string;
    status: string;
    submitted_at: string;
}
