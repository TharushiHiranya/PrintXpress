# Test plan

This document sets out how PrintXpress is tested. It covers the approach, the environment, the test data, and the test cases. It maps to Task E of the assignment. Each test case links back to a requirement id from `03-requirements.md` so coverage is easy to check.

## Approach

Testing happens at three levels, from small to large.

1. Unit tests check single pieces of logic in isolation, for example password hashing, price calculation, and promotion validity. These run on the JVM with JUnit and are fast.
2. Instrumented and UI tests check the app on a device or emulator, for example that a Room insert and read work, and that a screen shows the right content. These use AndroidX Test and Compose UI testing.
3. Manual tests follow the test cases below by hand on an emulator. They confirm the full user journeys end to end and catch anything the automated tests miss.

The aim is to cover every Must requirement with at least one test, and to test both the happy path and the common error paths such as a duplicate email or a missing file.

## Environment

| Item | Value |
|------|-------|
| Device | Android emulator, Pixel 6, and one physical phone if available |
| Android version | API 24 (minimum) and a recent API for the main runs |
| Build | Debug build from Android Studio |
| Data | A fresh install with the seeded catalog, plus a test account created during testing |
| Tools | JUnit, AndroidX Test, Compose UI test, and manual checks |

## Test data

The same data sets are reused across cases so results are repeatable.

| Set | Field | Value |
|-----|-------|-------|
| Valid user | name, email, phone, password | Nimal Perera, nimal@example.com, 0771234567, Print123 |
| Duplicate user | email | nimal@example.com (already registered) |
| Invalid email | email | nimal.example.com (no at sign) |
| Short password | password | 123 (under six characters) |
| Valid address | label, line1, city, postal | Home, 25 Galle Road, Colombo, 00300 |
| Valid order | product, quantity, size, material | Business cards, 100, A6, glossy |
| Design file | file | sample-card.png (a small valid image) |
| Promotion (active) | code, dates | FESTIVE10, valid now |
| Promotion (expired) | code, dates | OLD5, valid in the past |

## Test cases

Status starts as Not run and is set to Pass or Fail when the case is carried out.

### Authentication and profile

| Id | Requirement | Steps | Test data | Expected result | Status |
|----|-------------|-------|-----------|-----------------|--------|
| TC-01 | FR-01, FR-03 | Register a new account | Valid user | Account is created, password is stored hashed, home screen opens | Not run |
| TC-02 | FR-02 | Register with an email already in use | Duplicate user | Register is blocked with a clear message | Not run |
| TC-03 | FR-01, NFR-08 | Register with a bad email | Invalid email | Form shows a validation error and does not save | Not run |
| TC-04 | NFR-08 | Register with a short password | Short password | Form shows a validation error and does not save | Not run |
| TC-05 | FR-04 | Log in with correct details | Valid user | Login succeeds and home screen opens | Not run |
| TC-06 | FR-04 | Log in with a wrong password | Valid user, wrong password | Login fails with one clear message | Not run |
| TC-07 | FR-05 | Close and reopen the app while logged in | Valid user | The app opens still logged in | Not run |
| TC-08 | FR-06 | Edit the profile name and save | Valid user | The new name shows and is saved | Not run |
| TC-09 | FR-07 | Add an address and set it as default | Valid address | The address is saved and marked default | Not run |
| TC-10 | FR-08 | Log out | Valid user | Login state clears and the login screen shows | Not run |

### Browsing

| Id | Requirement | Steps | Test data | Expected result | Status |
|----|-------------|-------|-----------|-----------------|--------|
| TC-11 | FR-09 | Open the home screen | Seeded catalog | Product categories show, loaded from the local database | Not run |
| TC-12 | FR-10 | Open a category | Business cards | The product list for that category shows | Not run |
| TC-13 | FR-11 | Open a product | Business cards | Name, price, material, sample image, and options show | Not run |
| TC-14 | FR-12 | Filter products by material | glossy | The list updates to matching products, and clearing it restores the full list | Not run |

### Ordering

| Id | Requirement | Steps | Test data | Expected result | Status |
|----|-------------|-------|-----------|-----------------|--------|
| TC-15 | FR-13 | Upload a design file | Design file | The chosen file name shows and is saved with the item | Not run |
| TC-16 | FR-14 | Enter custom text instead of a file | Text only | The text is accepted and saved with the item | Not run |
| TC-17 | FR-15 | Try to add an item with no file and no text | Empty | The app blocks it with a clear message | Not run |
| TC-18 | FR-16 | Change quantity and material | Valid order | The price updates to match the selection | Not run |
| TC-19 | FR-17, FR-18 | Place a delivery order with an address | Valid order, valid address | The order saves with status processing and a delivery address | Not run |
| TC-20 | FR-19 | View the confirmation after placing an order | Valid order | The confirmation shows the order number and a summary | Not run |
| TC-21 | FR-17 | Try to place a delivery order with no address | Valid order, no address | The app blocks it and asks for an address | Not run |
| TC-22 | FR-20 | Save a design and reuse it on a new order | Design file | The saved design appears and can be picked again | Not run |

### Order management

| Id | Requirement | Steps | Test data | Expected result | Status |
|----|-------------|-------|-----------|-----------------|--------|
| TC-23 | FR-21, FR-22 | Open the orders screen | Existing orders | Orders show with date, total, and status | Not run |
| TC-24 | FR-23 | Cancel an order that is still processing | Processing order | The order moves to cancelled | Not run |
| TC-25 | FR-24 | Try to cancel an order that is printing | Printing order | Cancel is hidden or disabled with a short note | Not run |
| TC-26 | FR-23 | Reschedule an order that is still processing | Processing order | The reschedule is accepted and saved | Not run |

### Notifications and offers

| Id | Requirement | Steps | Test data | Expected result | Status |
|----|-------------|-------|-----------|-----------------|--------|
| TC-27 | FR-25, FR-26 | Place an order and open notifications | Valid order | A confirmation notification shows as unread, and opening it marks it read | Not run |
| TC-28 | FR-27 | Open the offers screen | Active promotion | The active offer shows with title, discount, code, and dates | Not run |
| TC-29 | FR-27 | Check an expired offer | Expired promotion | The expired offer does not show | Not run |

### Help and support

| Id | Requirement | Steps | Test data | Expected result | Status |
|----|-------------|-------|-----------|-----------------|--------|
| TC-30 | FR-28 | Open print guidelines | None | Guidance on file types, resolution, colour mode, and bleed shows | Not run |
| TC-31 | FR-29 | Open FAQs and contact | None | The FAQ list and the shop contact details show | Not run |

### Non-functional checks

| Id | Requirement | Steps | Expected result | Status |
|----|-------------|-------|-----------------|--------|
| TC-32 | NFR-02 | Turn off the network and use the app | Every screen works with no network | Not run |
| TC-33 | NFR-07 | Force an error such as a duplicate email | The app shows a message and does not crash | Not run |
| TC-34 | NFR-05 | Check touch targets and content descriptions | Buttons are easy to tap and icons have descriptions | Not run |
| TC-35 | NFR-10 | Export the database file and open it in DB Browser for SQLite | The tables and rows are visible | Not run |

## Defect handling

When a case fails, record the test id, what happened, the steps to reproduce, and a screenshot. Fix the cause, then run the failed case again and any related cases before marking it Pass.

## Exit criteria

Testing is complete when every Must requirement has at least one passing test, no known defect remains in a Must feature, and the offline, error handling, and database export checks all pass.
