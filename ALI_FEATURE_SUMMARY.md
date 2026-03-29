# Ali Feature Summary — Exchange Lifecycle Additions

This update adds four connected features to YorkU Circle in a single workflow:

1. **Request Materials**
   - Added to `/course-materials`
   - Students can request a missing textbook, lab kit, notes, or lab manual
   - Includes a live **Demand Board** and recent request feed for the searched course

2. **Confirm Exchange**
   - Added to `/inbox` and `/transactions`
   - Sellers can convert a buyer message into a tracked exchange using **Start Exchange**
   - Confirmation is **dual-sided**:
     - Seller confirms handoff
     - Buyer confirms receipt

3. **Transaction Status**
   - Added new `/transactions` page
   - Tracks each exchange through a separate transaction lifecycle:
     - Meetup pending
     - Awaiting buyer confirmation
     - Awaiting seller confirmation
     - Completed
     - Cancelled
     - Issue open
   - Listing status is linked but kept separate:
     - Transaction start -> listing becomes `UNAVAILABLE`
     - Transaction completed -> listing becomes `SOLD`
     - Transaction cancelled -> listing returns to `AVAILABLE`

4. **Report Issues**
   - Added inside each transaction card
   - Categories include no-show, damaged item, misleading description, payment issue, and other
   - Reporting an issue changes the transaction to **Issue open** and freezes completion

## New pages / entry points
- `/course-materials` -> Request Materials + Demand Board
- `/inbox` -> Start Exchange from seller inbox
- `/transactions` -> Track status, confirm exchange, cancel, and report issues

## Demo flow
1. Log in as `abc123@my.yorku.ca` / `pass123`
2. Open **Inbox**
3. Use **Start Exchange** on a seeded buyer message
4. Open **Transactions** and confirm the seller side
5. Log in as `student1@my.yorku.ca` / `welcome`
6. Open **Transactions** and confirm the buyer side
7. The transaction becomes completed and the listing becomes sold

## Notes
- Implemented for the existing `stub` profile so the demo works without database setup
- Added minimal bean annotations to existing feedback/block classes so the application can start cleanly
