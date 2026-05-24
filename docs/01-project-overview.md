# Project overview

## Introduction

PrintXpress is a native Android app for a digital printing service in Sri Lanka. It lets individuals and small businesses order printed items from their phone instead of visiting a shop or sending files over chat apps. Customers can browse products, choose specifications, upload a design or type custom text, and place an order for pickup or home delivery.

The app is built with Kotlin and Jetpack Compose, and it stores all data locally with Room on top of SQLite. It runs fully offline, so it works on any Android phone without a server or an internet connection.

## The problem

Small print shops in Sri Lanka usually take orders in person, over the phone, or through messaging apps. That makes it hard to show the full product range, hard to capture exact specifications, and easy to lose track of files and order status. Customers often do not know the file requirements for good print quality, so jobs get delayed by reworks.

PrintXpress fixes this by putting the catalog, the order form, the file upload, and the order tracking in one place. The customer sees prices and options up front, attaches the right file, picks pickup or delivery, and follows the order from processing to ready.

## Goals

The app exists to let a customer do four things well.

1. Browse printing products and services by type, material, and size.
2. Place an order by choosing specifications, uploading a file or entering text, and selecting pickup or delivery.
3. Manage their orders and read notifications about progress and offers.
4. Find design help through guidelines, sample designs, and FAQs.

## Who uses the app

The app is built for one type of user, the customer. There is no separate admin app in this version, which keeps the scope sensible for an assignment.

| Actor | Description |
|-------|-------------|
| Customer | A person or small business owner who browses products, places and tracks orders, manages their profile and saved designs, and reads offers. |
| System (background) | The app itself, which saves orders, updates order status, and raises notifications for confirmations and offers. This is not a person, it is the app acting on its own. |

## Scope

### In scope

The first version covers the full customer journey.

- Register and log in with email or phone, with passwords hashed before storage.
- Manage a profile, saved designs, and delivery addresses.
- Browse product categories with pricing, specifications, and sample previews.
- Place an order with quantity, paper or material type, and size, plus an uploaded file or custom text.
- Choose pickup or delivery, and pick a saved address for delivery.
- Track order status, and reschedule or cancel before printing starts.
- Receive in-app notifications for order confirmation, completion, and seasonal offers.
- Read print guidelines, view sample designs, and read FAQs.

### Out of scope

These are left out on purpose to keep the build simple and reliable.

- Online card payment. Orders are placed and paid for on collection or delivery.
- A separate staff or admin app. Order status changes are simulated inside the app for the demo.
- Real SMS or push delivery. Notifications are stored and shown inside the app.
- Cloud sync across devices. All data stays on the one device.

## How the app meets the assignment

The build maps directly onto the marked tasks for module CSE5011.

| Task | Where it is covered |
|------|---------------------|
| A. Compare mobile OS, tools, and technologies | `06-os-tools-comparison.md` |
| B. System and database design with UML and ER diagrams | `05-diagrams.md` and `diagrams/` |
| C. Attractive user interface | The Compose UI, designed around the brand colour and white background |
| D. Interactive app with database integration | The app code with Room storage |
| E. Test plan, test data, and testing | `07-test-plan.md` |
| F. User and technical documentation | `08-user-documentation.md` and `09-technical-documentation.md` |
