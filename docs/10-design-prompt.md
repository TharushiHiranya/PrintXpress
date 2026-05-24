# PrintXpress UI design brief

Use this document to design all screens for PrintXpress, a mobile printing service app. Every screen listed here needs a finished mockup. Follow the visual rules exactly so all screens feel like one coherent product.

---

## App overview

PrintXpress is a digital printing service app for Sri Lanka. Customers browse print products (business cards, posters, banners, flyers, stickers, mugs, t-shirts), upload a design or type custom text, place an order for pickup or delivery, and track it. The target user is a woman in her 20s to 40s running a small business or ordering personal prints. The design should feel polished, approachable, and a little elegant, not corporate or clinical.

---

## Visual identity

### Colours

| Role | Hex | Usage |
|------|-----|-------|
| Accent (primary) | `#A20BC8` | Buttons, active states, links, selected chips, key icons |
| Accent container | `#F6E7FA` | Card fills, chip backgrounds, selected row backgrounds, section tints |
| On accent | `#FFFFFF` | Text and icons placed directly on the accent colour |
| Background | `#FFFFFF` | Every screen background and card surface |
| Text primary | `#1A1A1A` | Headings, product names, prices |
| Text secondary | `#5C5C5C` | Captions, dates, supporting labels, descriptions |
| Text disabled | `#9A9A9A` | Inactive fields, placeholder text |
| Divider | `#E0E0E0` | Card borders, horizontal rules, separators |

The accent purple is rich and deliberate. Use `#F6E7FA` liberally for backgrounds on chips, selected cards, and section fills. This gives the UI its warmth. Never use the full purple `#A20BC8` as a full-screen background.

### Typography

One font family throughout. Clean and modern, similar to Google Sans or Nunito.

| Role | Size | Weight | Usage |
|------|------|--------|-------|
| Display | 36 sp | Light | Splash screen app name only |
| Headline | 28 sp | Semibold | Screen titles |
| Title large | 22 sp | Semibold | Product names, card headings |
| Title medium | 16 sp | Medium | Section labels, tab labels |
| Body large | 16 sp | Regular | Descriptions, form field text, list items |
| Body medium | 14 sp | Regular | Supporting text, FAQ answers |
| Label | 14 sp | Medium | Button labels |
| Caption | 11 sp | Regular | Timestamps, badge labels |

All text is **sentence case**. No ALL CAPS. No title case.

### Shape

Rounded corners throughout. This is a core part of the feel.

| Element | Corner radius |
|---------|--------------|
| Product cards, info cards | 16 dp |
| Buttons | 12 dp |
| Text input fields | 12 dp |
| Chips and status badges | Fully rounded (pill shape) |
| Bottom sheets | 24 dp on top corners only |
| Dialogs | 20 dp |
| Category icon containers | 16 dp |
| Avatar circles | Fully round |

### Logo placement

The logo is provided by the client. Use it in these places:

- **Splash screen**: centred on white, roughly 40% from the top, 180 dp wide.
- **Login and register screens**: centred at the top of the form, 80 dp wide.
- **Home screen top bar**: compact version, about 32 dp tall, left-aligned, replacing a text title.

Where the logo file is not yet available, use a rounded rectangle placeholder with `#A20BC8` fill and the white initials "PX" in title large. This is a temp stand-in only.

### Icons

Use a consistent rounded icon style (similar to Material Rounded or Phosphor Regular). Purple `#A20BC8` for key interactive icons, grey `#5C5C5C` for secondary icons.

---

## General layout rules

- Screen horizontal padding: 16 dp on both sides.
- Section vertical gap: 24 dp.
- Card internal padding: 16 dp.
- Gap between cards in a grid or list: 12 dp.
- Bottom navigation bar: white background, 1 dp top border in `#E0E0E0`, no heavy shadow.
- Touch targets at least 48 dp tall.
- Every screen has a white background unless stated otherwise.

---

## Screens to design

Design all screens below as mobile mockups at 390 × 844 dp (standard phone size).

---

### 1. Splash screen

- White background.
- App logo centred at roughly 40% from the top.
- App name "PrintXpress" in display size, `#A20BC8`, 16 dp below the logo.
- Tagline "Print, delivered." in body medium, `#5C5C5C`, 8 dp below the name.
- No buttons or navigation elements.

---

### 2. Login screen

- White background, no top app bar.
- Logo at the top, 80 dp wide, centred, 48 dp from screen top.
- "Welcome back" in headline, `#1A1A1A`, 24 dp below the logo.
- "Sign in to continue" in body medium, `#5C5C5C`, 8 dp below the heading.
- Two input fields below: "Email or phone" and "Password". Outlined style, `#E0E0E0` border at rest, `#A20BC8` border when focused. 12 dp rounded corners.
- Password field has a show/hide eye icon on the right.
- "Forgot password?" in `#A20BC8`, right-aligned, 8 dp below the password field.
- Full-width "Log in" button, 56 dp tall, `#A20BC8` fill, white label, 12 dp radius. 24 dp below the forgot link.
- "Don't have an account? Register" centred at the bottom of the screen. "Register" is in `#A20BC8`.

---

### 3. Register screen

Same overall layout as login.

- Logo at the top.
- "Create your account" heading, subheading "Join PrintXpress today".
- A toggle between "Email" and "Phone" as two pill chips side by side. Active chip is filled `#A20BC8` with white text. Inactive is `#F6E7FA` with `#A20BC8` text.
- Four fields: Full name, Email or Phone (depending on toggle), Password, Confirm password.
- Below the password field, a thin 4 dp tall colour bar as a password strength indicator. Red for weak, amber for fair, `#A20BC8` for strong.
- Full-width "Create account" button in `#A20BC8`.
- "Already have an account? Log in" at the bottom with "Log in" in `#A20BC8`.

---

### 4. Home screen

- White background.
- **Top bar**: logo (left), notification bell icon (right). Bell has a small `#A20BC8` dot badge when there are unread notifications.
- **Greeting**: "Good morning, Sarah" in title large, `#1A1A1A`. Below it, "What would you like to print today?" in body medium, `#5C5C5C`.
- **Search bar**: full width, rounded pill shape, `#E0E0E0` border, magnifier icon in `#5C5C5C` inside on the left, placeholder text "Search products..."
- **Offers section** (if active offers exist): section heading "Current offers" in title medium. Below it, horizontally scrollable row of offer cards. Each card is 280 dp wide, `#F6E7FA` background, 16 dp radius. Card contents: offer title in title medium `#1A1A1A`, short description in body medium `#5C5C5C`, discount code in a small `#A20BC8` chip, expiry date in caption `#5C5C5C`. A subtle `#A20BC8` left accent border (4 dp wide) on each offer card adds a premium touch.
- **Categories section**: heading "Browse by category" in title medium. Below it, a 2-column grid of category cards. Each card: white background, `#E0E0E0` border, 16 dp radius. Inside: a square `#F6E7FA` icon container at the top with a rounded icon in `#A20BC8`, category name in body large `#1A1A1A` below it.
  - Categories and icons: Business cards (ID badge icon), Posters (image frame icon), Banners (panorama icon), Flyers (document icon), Stickers (star icon), Mugs (coffee cup icon), T-shirts (shirt icon).
- **Bottom navigation bar**: 5 tabs. Home, Products, Orders, Alerts, Profile. Active tab icon and label in `#A20BC8`. Inactive in `#5C5C5C`.

---

### 5. Product list screen

- Top app bar: back arrow on the left, category name as title in title large, `#1A1A1A`.
- Below the app bar: a filter/search row. A search field on the left, a filter icon button on the right.
- Product cards in a 2-column grid, 12 dp gap between cards.
- Each product card: white background, `#E0E0E0` border, 16 dp radius, 8 dp padding.
  - Square image placeholder at the top: `#F6E7FA` background, rounded icon in `#A20BC8` centred (product category icon).
  - Product name in body large `#1A1A1A`.
  - Base price in body medium `#A20BC8` (e.g. "From LKR 350").
  - A small "Order" button at the bottom of the card, outlined `#A20BC8` border, `#A20BC8` text, pill shape.

---

### 6. Product detail screen

- Top app bar: back arrow, product name as title.
- Full-width product image at the top, 220 dp tall. `#F6E7FA` background placeholder with a large category icon centred.
- Product name in title large `#1A1A1A`, price in title large `#A20BC8`, both 16 dp below the image.
- Description in body medium `#5C5C5C`.
- **Spec selectors section**: "Choose your options" heading in title medium.
  - Size: label "Size" in body large, then a horizontal row of pill chips. Unselected: white background, `#E0E0E0` border. Selected: `#A20BC8` fill, white text.
  - Material/paper: same chip row style.
  - Quantity: a simple row with a minus button, the quantity number in title medium, and a plus button. The minus and plus buttons are outlined circles in `#A20BC8`.
- **Design section**: "Your design" heading. Two tab buttons side by side: "Upload file" and "Enter text". Active tab: `#F6E7FA` background, `#A20BC8` text and bottom underline. Inactive: white, `#5C5C5C` text.
  - Upload tab: a dashed bordered box, 120 dp tall, `#F6E7FA` background, upload icon in `#A20BC8` centred, "Tap to upload your design" label in body medium `#5C5C5C`. If a file is selected, show the filename with a remove (X) icon.
  - Text tab: a multiline text field, "Type your custom text here..." placeholder.
- **Sticky bottom bar**: white background, 1 dp top border `#E0E0E0`, slight shadow. Left side: live total price in title large `#A20BC8`. Right side: "Add to order" button, `#A20BC8` fill, white label, 12 dp radius, 48 dp tall.

---

### 7. Place order screen (3 steps)

**Step indicator at the top**: three numbered circles (1, 2, 3) joined by horizontal lines. Completed step: `#F6E7FA` circle, `#A20BC8` checkmark inside. Active step: `#A20BC8` filled circle, white number. Future step: `#E0E0E0` outline circle, `#9A9A9A` number.

**Step 1: Order summary**
- List of ordered items. Each item: card with product name, chosen specs (size, material, quantity), design file or text preview, and subtotal.
- Order total row at the bottom of the list: bold total in title large `#A20BC8`.
- "Next" button full width, `#A20BC8`.

**Step 2: Delivery**
- "How would you like to receive your order?" heading.
- Two large selection cards side by side: "Pickup" and "Delivery". Selected card: `#F6E7FA` background, `#A20BC8` border. Unselected: white, `#E0E0E0` border. Each card has an icon above the label.
- If "Delivery" is selected, a section appears below: "Delivery address". Shows saved addresses as selectable rows. Selected row has `#F6E7FA` background and `#A20BC8` left border (4 dp). A "+ Add new address" row in `#A20BC8` at the bottom.
- "Next" button full width, `#A20BC8`.

**Step 3: Confirm**
- Summary card: order items count, delivery method, address (if delivery), subtotal, delivery fee (if applicable), and grand total in title large `#A20BC8`.
- "Place order" button, full width, `#A20BC8`, 56 dp tall.

---

### 8. Order confirmation screen

- Centred layout, white background, no bottom nav visible.
- A large circle, 96 dp, `#F6E7FA` fill, with a checkmark icon in `#A20BC8` inside.
- "Order placed!" in headline `#1A1A1A`, centred, 16 dp below the circle.
- "Your order is being processed. We'll notify you when it's ready." in body medium `#5C5C5C`, centred.
- Order number in a `#F6E7FA` pill chip, e.g. "Order #1042", centred.
- Two buttons stacked: "View my orders" (filled `#A20BC8`) and "Back to home" (outlined `#A20BC8`).

---

### 9. Orders screen

- Top app bar: "My orders" title.
- A tab row below the app bar: "All", "Active", "Completed", "Cancelled". Active tab text and underline in `#A20BC8`. Inactive in `#5C5C5C`.
- Scrollable list of order cards. Each card: white background, `#E0E0E0` border, 16 dp radius, 16 dp padding.
  - Top row: order number in body large `#1A1A1A`, status badge on the right (pill chip, colour by status, see below).
  - Second row: date in caption `#5C5C5C`, item count in caption `#5C5C5C`.
  - Bottom row: total price in title medium `#A20BC8`, "View details" link in `#A20BC8` on the right.
- Status badge colours:
  - Processing: `#F6E7FA` background, `#A20BC8` text.
  - Printing: light amber background, dark amber text.
  - Ready for pickup: light green background, dark green text.
  - Out for delivery: light blue background, dark blue text.
  - Completed: `#F5F5F5` background, `#5C5C5C` text.
  - Cancelled: light red background, dark red text.

---

### 10. Order detail screen

- Top app bar: "Order #XXXX" as title, back arrow.
- **Status stepper**: a horizontal row showing all six status stages. Each stage has a small icon and a label below it. Stages to the left of the current one are filled `#A20BC8`. The current stage is `#A20BC8` with a pulsing ring. Future stages are `#E0E0E0`. Stages: Processing, Printing, Ready, Delivering, Completed.
- **Items section**: "Items in this order" heading. Card for each item with name, specs, and subtotal.
- **Delivery section**: "Delivery details" heading. Shows pickup or delivery address.
- **Total section**: line items (subtotal, delivery fee, total) aligned to the right. Total in title large `#A20BC8`.
- **Action row** (only visible if status is Processing): "Cancel order" button (outlined red) and "Reschedule" button (outlined `#A20BC8`) side by side. If status is past processing, show a small note in caption `#9A9A9A`: "Changes are not allowed once printing starts."

---

### 11. Notifications screen

- Top app bar: "Notifications" title. "Mark all read" text link on the right in `#A20BC8`.
- Scrollable list of notification rows.
- Unread rows: `#F6E7FA` background, a 4 dp `#A20BC8` left border on the row.
- Read rows: white background.
- Each row: notification title in body large `#1A1A1A` (bold if unread), message body in body medium `#5C5C5C`, timestamp in caption `#9A9A9A`, and a small icon on the left matching the notification type (order icon, offer icon, etc.) in `#A20BC8`.

---

### 12. Profile screen

- Top app bar: "Profile" title. Settings gear icon on the right.
- **Avatar section**: centred, large circle 72 dp, `#F6E7FA` fill, first letter of the user's name in title large `#A20BC8`. Below: user's full name in title large `#1A1A1A`, email or phone in body medium `#5C5C5C`. An "Edit profile" link in `#A20BC8` below that.
- **Menu rows**: a vertical list of tappable rows, each 56 dp tall. Left: icon in `#A20BC8`, label in body large `#1A1A1A`. Right: chevron arrow in `#5C5C5C`.
  - Delivery addresses
  - Saved designs
  - Print guidelines
  - FAQs and support
  - About PrintXpress
- **Log out row** at the bottom: icon in red, label in red, no chevron.

---

### 13. Edit profile screen

- Top app bar: "Edit profile", back arrow, "Save" text button on the right in `#A20BC8`.
- Fields: Full name, Email, Phone. All outlined style matching the login screen.
- A "Change password" section below with current password, new password, and confirm password fields.
- Validation errors appear in caption size red text below the relevant field.

---

### 14. Delivery addresses screen

- Top app bar: "Delivery addresses", back arrow.
- List of saved address cards. Each card: white background, `#E0E0E0` border, 16 dp radius.
  - Address text in body large `#1A1A1A`.
  - If it is the default: a "Default" pill badge in `#F6E7FA` and `#A20BC8` text.
  - Row of actions at the bottom of the card: "Edit" and "Delete" in `#5C5C5C`, "Set as default" in `#A20BC8` (hidden if already default).
- A "+ Add new address" card at the bottom, dashed `#A20BC8` border, "+" icon and label centred in `#A20BC8`.

---

### 15. Print guidelines screen

- Top app bar: "Print guidelines", back arrow.
- A scrollable list of expandable sections.
- Each section: a row with the topic title in body large `#1A1A1A`, a chevron icon on the right in `#A20BC8`. Tapping expands the body.
- Expanded body: body medium `#5C5C5C` text. `#F6E7FA` background on the expanded area, 1 dp `#E0E0E0` bottom border.
- Topics: Accepted file types, Resolution requirements, Colour mode (CMYK vs RGB), Bleed and margins, How to prepare your file.

---

### 16. FAQ and support screen

- Top app bar: "FAQs and support", back arrow.
- Same expandable accordion layout as the guidelines screen.
- Below the FAQ list: a "Contact us" section. Card with phone number and email, each row with a call or mail icon in `#A20BC8` and the contact detail in body large `#1A1A1A`.

---

## Interaction states to show

For at least the login screen and the product detail screen, show these states:

- **Default**: resting state.
- **Focused**: input field with `#A20BC8` border highlighted.
- **Error**: red border on a field, red error caption below.
- **Loading**: button in disabled/greyed state with a small spinner inside.
- **Empty state**: if an orders list is empty, show a centred illustration placeholder, a "No orders yet" heading, and a "Browse products" button.

---

## What to deliver

One mockup per screen, at 390 × 844 dp. All screens must use the same colour palette, typography, and component styles. Screens should look like they belong to one app, not a collection of experiments.
