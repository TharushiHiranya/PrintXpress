# User stories

These stories describe what a customer needs from PrintXpress and how we know each one is done. They are grouped by feature area. Each story has an id, the story itself, a priority, and acceptance criteria that the app must meet.

Priority uses MoSCoW: Must, Should, or Could.

## Accounts and profile

### US-01 Register an account
**Must.** As a new customer, I want to register with my email or phone number and a password, so that I can save my orders and designs.

Acceptance criteria:
1. The form takes a full name, an email or phone number, and a password.
2. The email or phone must be unique. A duplicate shows a clear message.
3. The password is hashed before it is saved, never stored as plain text.
4. After a successful register, the customer lands on the home screen, logged in.

### US-02 Log in
**Must.** As a returning customer, I want to log in with my email or phone and password, so that I can reach my account.

Acceptance criteria:
1. Correct details log the customer in and open the home screen.
2. Wrong details show one clear error without saying which field was wrong.
3. The login state survives closing and reopening the app.

### US-03 Manage profile
**Should.** As a customer, I want to view and edit my name, email, and phone, so that my details stay correct.

Acceptance criteria:
1. The profile screen shows the current name, email, and phone.
2. Edits are saved to the database and shown straight away.
3. Changing the email or phone to one already in use is blocked with a message.

### US-04 Manage delivery addresses
**Should.** As a customer, I want to save and edit delivery addresses, so that I can check out faster.

Acceptance criteria:
1. The customer can add, edit, and delete addresses.
2. One address can be marked as the default.
3. The default address is pre-selected during delivery checkout.

### US-05 Log out
**Should.** As a customer, I want to log out, so that my account is safe on a shared phone.

Acceptance criteria:
1. Log out clears the login state and returns to the login screen.
2. After log out, account screens cannot be reached without logging in again.

## Browsing products

### US-06 Browse product categories
**Must.** As a customer, I want to see categories of print products, so that I can find what I need.

Acceptance criteria:
1. The home screen lists categories such as business cards, posters, banners, flyers, stickers, mugs, and t-shirts.
2. Each category opens a list of products in that category.
3. The catalog loads from the local database, with no internet needed.

### US-07 View product details
**Must.** As a customer, I want to open a product and see its price, specifications, and a sample preview, so that I can decide what to order.

Acceptance criteria:
1. The detail screen shows the name, description, base price, material, and a sample image.
2. The available options for size, paper or material, and quantity are visible.
3. An add to order or order now action is available from this screen.

### US-08 Search or filter products
**Could.** As a customer, I want to filter products by type, material, or size, so that I can narrow the list quickly.

Acceptance criteria:
1. A filter or search control is available on the product list.
2. The list updates to match the chosen filter.
3. Clearing the filter shows the full list again.

## Designs and ordering

### US-09 Upload a design file
**Must.** As a customer, I want to upload my own design file, so that the shop prints exactly what I want.

Acceptance criteria:
1. The customer can pick an image or PDF from the phone.
2. The chosen file name is shown so the customer can confirm it.
3. The file reference is saved with the order item.

### US-10 Enter custom text
**Must.** As a customer, I want to type custom text instead of a file, so that I can order simple jobs without a design.

Acceptance criteria:
1. A text field is available as an alternative to a file upload.
2. Either a file or text is required before the item can be added.
3. The text is saved with the order item.

### US-11 Place an order
**Must.** As a customer, I want to choose quantity, paper or material, and size, then place the order, so that my print job is requested.

Acceptance criteria:
1. The customer selects quantity, material, and size, and the price updates to match.
2. The customer chooses pickup or delivery, and a delivery address if needed.
3. On confirm, the order and its items are saved to the database with the status set to processing.
4. A confirmation screen shows the order number and a summary.

### US-12 Save a design for reuse
**Could.** As a customer, I want to save a design to my account, so that I can reuse it on a later order.

Acceptance criteria:
1. An uploaded design can be saved with a title.
2. Saved designs appear in the profile and can be picked during a new order.
3. A saved design can be deleted.

## Managing orders

### US-13 Track order status
**Must.** As a customer, I want to see the status of each order, so that I know when it will be ready.

Acceptance criteria:
1. The orders screen lists all of the customer's orders with date, total, and status.
2. Status values are processing, printing, ready for pickup, out for delivery, completed, and cancelled.
3. Opening an order shows its items and the full status.

### US-14 Reschedule or cancel an order
**Must.** As a customer, I want to cancel or reschedule an order before printing starts, so that I can fix mistakes.

Acceptance criteria:
1. Cancel and reschedule are available only while the status is processing.
2. Once the status is printing or later, both actions are hidden or disabled with a short note.
3. A cancelled order shows the cancelled status and cannot be edited.

## Notifications and offers

### US-15 Receive order notifications
**Must.** As a customer, I want notifications when my order is confirmed and completed, so that I do not have to keep checking.

Acceptance criteria:
1. Placing an order creates a confirmation notification.
2. A status change to completed creates a completion notification.
3. Notifications show in a list with an unread marker, and tapping one marks it read.

### US-16 See seasonal offers
**Should.** As a customer, I want to see seasonal offers and discount codes, so that I can save money on bulk or festive orders.

Acceptance criteria:
1. Active offers appear on the home screen or an offers screen.
2. Each offer shows its title, discount, code, and valid dates.
3. Expired offers do not appear.

## Help and support

### US-17 Read print guidelines
**Should.** As a customer, I want guidance on file requirements, colour formats, and bleed margins, so that my print comes out right.

Acceptance criteria:
1. A guidelines screen explains accepted file types, resolution, colour mode, and bleed.
2. The guidance is written in plain language.

### US-18 Read FAQs and contact support
**Should.** As a customer, I want FAQs and a way to ask for help, so that I can solve design questions.

Acceptance criteria:
1. An FAQ screen lists common questions and answers.
2. A contact option shows the shop's phone or email for design queries.

## Story summary

| Id | Story | Priority |
|----|-------|----------|
| US-01 | Register an account | Must |
| US-02 | Log in | Must |
| US-03 | Manage profile | Should |
| US-04 | Manage delivery addresses | Should |
| US-05 | Log out | Should |
| US-06 | Browse product categories | Must |
| US-07 | View product details | Must |
| US-08 | Search or filter products | Could |
| US-09 | Upload a design file | Must |
| US-10 | Enter custom text | Must |
| US-11 | Place an order | Must |
| US-12 | Save a design for reuse | Could |
| US-13 | Track order status | Must |
| US-14 | Reschedule or cancel an order | Must |
| US-15 | Receive order notifications | Must |
| US-16 | See seasonal offers | Should |
| US-17 | Read print guidelines | Should |
| US-18 | Read FAQs and contact support | Should |
