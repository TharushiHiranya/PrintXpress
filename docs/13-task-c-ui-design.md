# Task C: User interface design

## Introduction

The PrintXpress user interface was designed in two stages. The first stage was a high-fidelity prototype built in Figma, which established the visual language, layout structure, and component library before any code was written. The second stage was the implementation inside Android Studio using Jetpack Compose, where each prototype screen was translated into working native UI code.

The design follows Material Design 3 with a single brand accent colour, `#A20BC8`, on a white background. Every screen uses the same type scale, spacing system, and colour tokens so the app feels consistent from the first screen to the last. The bottom navigation bar, the top app bar, and the accent-coloured buttons appear on every applicable screen to reinforce that consistency.

---

## Part 1: Figma prototypes

The initial designs were created in Figma as a single-page HTML export containing all screens. Each screen was laid out at a standard Android phone viewport (390 x 844 dp), with real content and interactive state variations (empty states, loading states, error states) shown side by side.

The prototype covered the full user journey, from the splash screen through authentication, product browsing, order placement, order tracking, and account management. Design decisions made at this stage, such as the category card style, the offer card layout, the dotted upload zone in the product detail screen, and the four-segment password strength indicator, were carried forward unchanged into the Compose implementation.

**[Insert Figma prototype screenshots here]**

---

## Part 2: Android Studio implementation (Compose Design)

All screens were built with Jetpack Compose using the `@Composable` function pattern. State is managed through `ViewModel` classes with `mutableStateOf`, and navigation uses the Compose Navigation component. The sections below describe each screen as it appears in the running app.

---

### 1. Splash screen

The splash screen is the app's entry point. It shows the PrintXpress logo centred on a white background with the tagline "Your print, your way" in secondary text below. After a two-second delay the screen checks whether a session is active and navigates to the home screen for returning users, or to the login screen for new ones.

The logo is displayed at 180 dp and there are no interactive elements on this screen.

**[Insert Splash screen screenshot here]**

---

### 2. Login screen

The login screen lets existing customers sign in with their email address or phone number and a password. The screen opens with the logo at 100 dp centred at the top, followed by a "Welcome back" heading and a short subtitle.

Below the logo there are two labelled text fields: one for the email or phone number, and one for the password. The password field has a show/hide toggle icon on the trailing edge. A filled "Log in" button in the brand accent colour sits below the fields. An error message appears in red text between the fields and the button when credentials are incorrect. A "Don't have an account? Register" link at the bottom navigates to the register screen. The screen is scrollable to accommodate small displays and the soft keyboard.

**[Insert Login screen screenshot here]**

---

### 3. Register screen

The register screen lets new customers create an account. It shares the same logo and header layout as the login screen but shows a "Create account" heading.

The form collects four fields: full name, email address or phone number, password, and confirm password. Below the password field, a four-segment strength bar fills left to right as the password becomes stronger, changing colour from red through amber to green. Validation runs on each field as the user leaves it, and a red inline error message appears beneath any field that fails. A filled "Create account" button submits the form. A "By continuing you agree to our Terms and Privacy Policy" note in small secondary text sits below the button. An inline error banner appears at the top of the form if the server returns a registration failure.

**[Insert Register screen screenshot here]**

---

### 4. Home screen

The home screen is the main landing page after login. It uses the `MainScaffold` wrapper, which provides the top app bar showing the PrintXpress logo and a notification bell icon with a badge showing unread count, and the bottom navigation bar with four tabs: Home, Orders, Notifications, and Profile.

The content area is a scrollable column that starts with a personalised greeting ("Good morning, [first name]") and a subtitle. A full-width rounded search field with a search icon sits below the greeting. Users can type a product name and submit to jump directly to the product list filtered by that query.

Below the search field, a "Categories" heading introduces a horizontal scrolling row of category cards. Each card is a rounded rectangle with an `AccentContainer` fill, a 56 dp icon container at the top, and the category name in primary text below. The categories shown are business cards, posters, banners, flyers, stickers, mugs, and t-shirts.

If there are active promotions, a "Current offers" heading appears below the category row, followed by offer cards. Each offer card has a 4 dp left accent bar, a title, a short description, and a discount label on a light accent background, with a white circle decoration in the bottom-right corner.

**[Insert Home screen screenshot here]**

---

### 5. Product list screen

The product list screen shows all products in a selected category. The top app bar has a back arrow and the category name as the title.

A search field and a row of filter chips sit below the top bar. The chips let users filter by material type. Below the chips, products are shown in a two-column grid. Each product card displays the product name, the base price per unit, and a small category icon. A heart icon in the top-right corner of each card lets users save a product to their saved designs list. Tapping a card navigates to the product detail screen.

An empty state illustration and message appear when no products match the active filter.

**[Insert Product list screen screenshot here]**

---

### 6. Product detail screen

The product detail screen gives customers all the information they need to place an order. The top app bar has a back arrow, the product name, and a bookmark icon on the trailing edge for saving the product.

The first section shows a sample image preview card with a "View samples" label. Below it, the product name, price, and a short description are displayed in a card. A dotted-border upload zone labelled "Upload your design" sits below the description. Users can tap this zone to attach a file or type custom text in the field below it.

Below the upload zone, a row of size chips lets users pick from the product's available sizes. A second row of material chips covers paper or material options. A quantity counter with minus and plus buttons sits below the chips. A delivery info banner in accent-container fill notes the estimated turnaround time.

A full-width "Add to order" button in the brand accent colour at the bottom navigates to the place order screen with the chosen specs pre-filled.

**[Insert Product detail screen screenshot here]**

---

### 7. Place order screen

The place order screen is where customers confirm their order and choose how they want to receive it. The top app bar shows "Place order" as the title with a back arrow.

The screen is divided into three sections. The first section is an order summary card showing the product name, quantity, size, material, custom text (if entered), unit price, and calculated total. If a promotion is active for the product, a discount line appears below the subtotal showing the saving.

The second section is the delivery section. A toggle lets the user switch between "Pickup" and "Delivery". In pickup mode, the shop address and hours are shown. In delivery mode, a list of the user's saved addresses appears with radio buttons; a "Add new address" link opens the address entry screen. A promo code field with an "Apply" button sits below the delivery section; a green confirmation or red error message shows the result.

The third section is a "Place order" button in the accent colour. While the order is being saved, the button shows a circular progress indicator and is disabled to prevent double-submission.

**[Insert Place order screen screenshot here]**

---

### 8. Order confirmation screen

The order confirmation screen gives the customer a clear acknowledgement that their order was received. A large check icon in the accent colour sits at the top of the content area. Below it, a heading reads "Order placed!" and a subtext shows the order reference number.

A summary card below the confirmation shows the product name, quantity, total paid, and the delivery method. Two buttons at the bottom let the user go to "View order" or "Back to home". The screen has no back navigation in the top bar to prevent accidental re-submission.

**[Insert Order confirmation screen screenshot here]**

---

### 9. Orders screen

The orders screen lists every order the customer has placed, most recent first. The top app bar shows "My orders" with a back arrow, and the `MainScaffold` bottom navigation bar is visible.

Each order appears as a card row showing the order number, the date, the total amount, and a coloured status badge. The status badge uses a fixed colour scheme: Processing uses grey, Printing uses amber, Ready for pickup uses green, Out for delivery uses blue, and Cancelled uses red. A search field at the top lets the user filter by order number or product name.

An empty state message with a "Start shopping" button appears when the customer has no orders yet.

**[Insert Orders screen screenshot here]**

---

### 10. Order detail screen

The order detail screen shows the full history of a single order. The top app bar shows the order number as the title with a back arrow.

At the top of the content, a hero card in `AccentContainer` fill shows the current status label and a short status description. Below it, a progress stepper shows five stages: Processing, Printing, Ready, Delivering, and Completed. Completed stages have a filled accent icon; the current stage has an outlined accent icon; future stages use a grey icon. Each stage label is shown below its icon.

Below the stepper, an items section lists every product in the order with its quantity, size, material, and line total. A delivery section shows either the pickup address or the delivery address. A totals section at the bottom shows the subtotal, any discount applied, and the final total. If the order status is still in Processing, a red "Cancel order" outlined button appears at the bottom.

**[Insert Order detail screen screenshot here]**

---

### 11. Profile screen

The profile screen is the account hub. The top app bar shows "Profile" and the `MainScaffold` bottom navigation is present.

At the top of the content, a circular avatar placeholder shows the user's initials in the accent colour on a light accent background. The user's full name and email or phone number sit below the avatar. Below the name, a stats card in a rounded outlined container shows three figures side by side: total orders placed, lifetime spend in rupees, and the customer tier (Standard or Premium).

Below the stats card, a list of menu rows lets the user navigate to: edit profile, delivery addresses, saved designs, print guidelines, FAQs and support, and about PrintXpress. Each row has a leading icon, a label, and a trailing chevron. A red "Log out" row with a logout icon sits at the bottom of the list.

**[Insert Profile screen screenshot here]**

---

### 12. Edit profile screen

The edit profile screen lets customers update their account details. The top app bar shows "Edit profile" with a back arrow.

Three text fields are shown: full name, email address, and phone number, each pre-filled with the current values. A "Save changes" button in the accent colour sits below the fields. Inline error messages appear beneath fields that fail validation (for example, a badly formatted email address). A success snackbar appears at the bottom of the screen after a successful save.

**[Insert Edit profile screen screenshot here]**

---

### 13. Addresses screen

The addresses screen lets customers manage the delivery addresses saved to their account. The top app bar shows "Delivery addresses" with a back arrow.

Each address is shown as a card with the recipient name, the full address text, and the phone number. An "Add address" floating action button in the accent colour appears at the bottom right. Tapping a saved address expands an action row with "Edit" and "Delete" buttons. A confirmation dialog appears before a delete is carried out. The address marked as default has a small "Default" chip below the address text.

**[Insert Addresses screen screenshot here]**

---

### 14. Saved designs screen

The saved designs screen shows the products the customer has bookmarked for future use. The top app bar shows "Saved designs" with a back arrow.

Products are displayed in the same two-column card grid used on the product list screen. Each card has a product name, price, and a filled bookmark icon in the accent colour to show the saved state. Tapping a card navigates to the product detail screen. A red trash icon in the top-right corner of each card lets the customer remove the bookmark. An empty state message with a "Browse products" button appears when no designs have been saved.

**[Insert Saved designs screen screenshot here]**

---

### 15. Notifications screen

The notifications screen shows order updates and promotional alerts. The top app bar shows "Notifications" with a back arrow, and the `MainScaffold` bottom navigation is present.

Each notification is a row with a left border: a 4 dp `AccentContainer`-filled border for unread items, and a transparent border for read ones. The row shows a notification icon, a bold title, a short description, and the relative time (for example, "2 hours ago"). Tapping a notification that relates to an order navigates to that order's detail screen.

An empty state message appears when there are no notifications. A "Mark all as read" text button appears in the top-right area of the app bar when unread items are present.

**[Insert Notifications screen screenshot here]**

---

### 16. Print guidelines screen

The print guidelines screen explains the technical requirements for design files. The top app bar shows "Print guidelines" with a back arrow.

The content is a scrollable list of guideline sections, each in an expandable card. The sections cover: accepted file formats (PDF, PNG, JPEG, AI, PSD), minimum resolution (300 DPI for print), colour mode (CMYK recommended over RGB), bleed and margin requirements, and maximum file size. Each section header is shown in bold primary text. The body text uses bullet points to list each rule clearly. A "Download templates" outlined button in the accent colour appears at the bottom, offering printable setup templates.

**[Insert Print guidelines screen screenshot here]**

---

### 17. FAQ screen

The FAQ screen covers common questions and provides a contact route for customers who need further help. The top app bar shows "FAQs and support" with a back arrow.

Questions are displayed as expandable rows using an accordion pattern. Each row shows the question in primary text and a chevron that rotates when the row is open. The answer text appears below the question in secondary text colour when expanded. Only one answer is open at a time. The FAQ topics cover ordering, payments, file uploads, delivery, and cancellations.

A "Contact us" section at the bottom of the screen shows a phone number and an email address as tappable links. Tapping the phone number opens the dialler and tapping the email opens the mail client.

**[Insert FAQ screen screenshot here]**

---

### 18. About screen

The about screen gives background on the PrintXpress service. The top app bar shows "About PrintXpress" with a back arrow.

The screen shows the PrintXpress logo at 80 dp, the app version number in secondary text, and a short paragraph describing the service: a Sri Lankan digital printing shop that helps individuals and small businesses produce high-quality print materials quickly and affordably. Below the description, a "Visit our website" outlined button links to the business website, and a "Rate the app" text button links to the Play Store listing.

**[Insert About screen screenshot here]**

---

## Design decisions

**Colour.** A single accent colour, `#A20BC8`, is used for all interactive elements: buttons, selected chips, active navigation icons, progress bars, and status highlights. This keeps the interface simple and makes the brand instantly recognisable. Light-accent `#F6E7FA` fills are used for chips and cards that need to be visually distinct without competing with actionable elements.

**Typography.** Material 3's default type scale is used without customisation. `titleLarge` and `titleMedium` are used for headings, `bodyMedium` for body copy, and `labelSmall` for badges and captions. This ensures readable contrast ratios across all text sizes.

**Navigation.** Bottom navigation covers the four main areas: Home, Orders, Notifications, and Profile. A back arrow in the top app bar handles every secondary screen. This matches the standard Android navigation pattern that users already know.

**Status colours.** Order status badges use a fixed four-colour system (grey, amber, green, blue, and red) so customers can read the status at a glance without reading the label text.

**Empty states.** Every list screen has a dedicated empty state with an icon, a short message, and a call-to-action button. This avoids showing a blank screen when a list has no items and guides the user towards the next sensible action.

**Validation.** All forms validate on field blur rather than on submit. Error messages appear inline below the relevant field in the standard Material 3 error style so the user knows exactly what to fix.
