# Requirements

This document lists what PrintXpress must do (functional requirements) and the qualities it must have (non-functional requirements). Each requirement has an id so it can be traced to user stories, diagrams, and test cases.

## Functional requirements

### Authentication and profile

| Id | Requirement | Stories |
|----|-------------|---------|
| FR-01 | The app shall let a customer register with a full name, an email or phone number, and a password. | US-01 |
| FR-02 | The app shall reject a register attempt that uses an email or phone already in the database. | US-01, US-03 |
| FR-03 | The app shall hash every password before saving it, and never store plain text. | US-01 |
| FR-04 | The app shall let a customer log in with their email or phone and password. | US-02 |
| FR-05 | The app shall keep a customer logged in until they log out. | US-02, US-05 |
| FR-06 | The app shall let a customer view and edit their name, email, and phone. | US-03 |
| FR-07 | The app shall let a customer add, edit, delete, and set a default delivery address. | US-04 |
| FR-08 | The app shall let a customer log out, which clears the login state. | US-05 |

### Browsing

| Id | Requirement | Stories |
|----|-------------|---------|
| FR-09 | The app shall show product categories loaded from the local database. | US-06 |
| FR-10 | The app shall show a list of products for a chosen category. | US-06 |
| FR-11 | The app shall show product details including name, description, base price, material, sample image, and the size, material, and quantity options. | US-07 |
| FR-12 | The app shall let a customer filter or search the product list by type, material, or size. | US-08 |

### Ordering

| Id | Requirement | Stories |
|----|-------------|---------|
| FR-13 | The app shall let a customer upload an image or PDF design file and show the chosen file name. | US-09 |
| FR-14 | The app shall let a customer enter custom text in place of a file. | US-10 |
| FR-15 | The app shall require either a file or custom text before an item is added to an order. | US-09, US-10 |
| FR-16 | The app shall let a customer choose quantity, material or paper, and size, and shall update the price to match. | US-11 |
| FR-17 | The app shall let a customer choose pickup or delivery, and select a saved address for delivery. | US-11, US-04 |
| FR-18 | The app shall save the order and its items to the database with the status set to processing. | US-11 |
| FR-19 | The app shall show an order confirmation with the order number and a summary. | US-11 |
| FR-20 | The app shall let a customer save a design with a title and reuse it on a later order. | US-12 |

### Order management

| Id | Requirement | Stories |
|----|-------------|---------|
| FR-21 | The app shall list a customer's orders with date, total, and status. | US-13 |
| FR-22 | The app shall show order status as one of processing, printing, ready for pickup, out for delivery, completed, or cancelled. | US-13 |
| FR-23 | The app shall allow cancel and reschedule only while the order status is processing. | US-14 |
| FR-24 | The app shall hide or disable cancel and reschedule once the status is printing or later. | US-14 |

### Notifications and offers

| Id | Requirement | Stories |
|----|-------------|---------|
| FR-25 | The app shall create a notification when an order is placed and when it is completed. | US-15 |
| FR-26 | The app shall show notifications in a list with an unread marker, and mark one read when it is opened. | US-15 |
| FR-27 | The app shall show active seasonal offers with title, discount, code, and valid dates, and hide expired offers. | US-16 |

### Help and support

| Id | Requirement | Stories |
|----|-------------|---------|
| FR-28 | The app shall show print guidelines covering file types, resolution, colour mode, and bleed. | US-17 |
| FR-29 | The app shall show an FAQ list and the shop's contact details. | US-18 |

## Non-functional requirements

| Id | Quality | Requirement |
|----|---------|-------------|
| NFR-01 | Platform | The app shall run on Android 7.0 (API 24) and above, built with native Kotlin and Jetpack Compose only. |
| NFR-02 | Offline | The app shall work fully offline. All data is stored on the device with Room over SQLite. |
| NFR-03 | Performance | Common screens shall open in under one second on a mid-range phone, and lists shall scroll smoothly. |
| NFR-04 | Usability | The UI shall use one accent colour on a white background with dark gray text, consistent labels, and sentence case throughout. |
| NFR-05 | Accessibility | Text shall meet a readable contrast against white, touch targets shall be at least 48 by 48 dp, and every icon button shall have a content description. |
| NFR-06 | Security | Passwords shall be hashed. No sensitive data shall be logged or shown in plain text. |
| NFR-07 | Reliability | A failed action, such as a duplicate email or a missing file, shall show a clear message and shall not crash the app. |
| NFR-08 | Validation | All input forms shall validate required fields and formats before saving. |
| NFR-09 | Maintainability | Code shall be grouped by purpose (data, ui, viewmodel), use simple patterns, and carry short comments. |
| NFR-10 | Portability | The database shall be a single local SQLite file that can be exported and opened with DB Browser for SQLite. |

## Validation rules

These rules back FR-02, FR-15, FR-16, and NFR-08.

1. Full name is required and at least two characters.
2. Email, when used, matches a basic email pattern. Phone, when used, is digits only and 9 to 12 characters long.
3. Password is at least six characters.
4. An order item needs a file or custom text, a quantity of at least one, and a selected size and material.
5. A delivery order needs a selected address.
