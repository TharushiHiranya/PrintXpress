# Claude Code build prompts

This document contains six prompts for building PrintXpress in Claude Code, one phase at a time. Complete each phase fully and confirm it compiles before moving to the next. Each prompt is self-contained so you can copy and paste it directly into Claude Code.

The six phases are:

1. Foundation (project setup, theme, navigation skeleton)
2. Auth screens (splash, login, register, pure UI)
3. All remaining UI screens (home through FAQ, pure UI, static data)
4. Database layer (Room entities, DAOs, database class, seeding, repositories)
5. Auth and product logic (ViewModels, connect auth and browsing to Room)
6. Orders, notifications, and profile logic (wire the remaining features)

Complete phases in order. Do not skip ahead.

---

## Phase 1: Foundation

Copy this entire prompt into Claude Code.

---

You are helping me build PrintXpress, a native Android app for a digital printing service. Read `CLAUDE.md` before doing anything. It has the full tech stack, package name, colours, rules, and project structure. Follow it exactly.

In this phase, set up the project foundation only. Do not build any screens yet beyond a simple placeholder.

**Step 1: Dependencies**

Ask me to approve the following additions to `gradle/libs.versions.toml` and the app-level `build.gradle.kts` before writing any code:

- Jetpack Navigation Compose (for screen-to-screen navigation)
- Room (for local database, needed in a later phase but add it now so the project compiles cleanly throughout)
- The Room KSP annotation processor

Tell me the exact version strings you plan to use and wait for my reply before continuing.

**Step 2: Theme**

Create `app/src/main/java/com/hiranya/printxpress/ui/theme/Color.kt` with these named colours and no others:

```
Accent          = #A20BC8
AccentContainer = #F6E7FA
OnAccent        = #FFFFFF
Background      = #FFFFFF
TextPrimary     = #1A1A1A
TextSecondary   = #5C5C5C
TextDisabled    = #9A9A9A
Divider         = #E0E0E0
```

Create `ui/theme/Type.kt` with a Material 3 typography set. Use `FontFamily.Default` for now. Set these sizes and weights to match the design spec:
- `displaySmall` at 36 sp, Light weight (splash screen app name)
- `headlineMedium` at 28 sp, SemiBold (screen titles, auth headings)
- `titleLarge` at 22 sp, SemiBold (product names, card headings)
- `titleMedium` at 16 sp, Medium (section labels)
- `bodyLarge` at 16 sp, Normal (descriptions, list items)
- `bodyMedium` at 14 sp, Normal (supporting text)
- `labelMedium` at 14 sp, Medium (button labels)
- `labelSmall` at 11 sp, Normal (timestamps, badge labels)

Create `ui/theme/Theme.kt` that builds a Material 3 light colour scheme mapping:
- primary → Accent
- onPrimary → OnAccent
- primaryContainer → AccentContainer
- background and surface → Background
- onBackground and onSurface → TextPrimary

Keep dark theme off for now.

**Step 3: Navigation skeleton**

Create `app/src/main/java/com/hiranya/printxpress/ui/screens/` and add one file per screen below. Each file should contain a single `@Composable` function that shows only a centred `Text` with the screen name. No real UI yet.

Screens to scaffold:
- SplashScreen
- LoginScreen
- RegisterScreen
- HomeScreen
- ProductListScreen (takes a categoryId: Long parameter)
- ProductDetailScreen (takes a productId: Long parameter)
- PlaceOrderScreen (takes a productId: Long parameter)
- OrderConfirmationScreen (takes an orderId: Long parameter)
- OrdersScreen
- OrderDetailScreen (takes an orderId: Long parameter)
- NotificationsScreen
- ProfileScreen
- EditProfileScreen
- AddressesScreen
- PrintGuidelinesScreen
- FaqScreen

Create `app/src/main/java/com/hiranya/printxpress/ui/Navigation.kt`. Define a `sealed class Screen` with a route string for each screen above. Wire them into a `NavHost` inside a `PrintXpressNavGraph` composable. The start destination is `SplashScreen`.

**Step 4: MainActivity**

Update `MainActivity.kt` to apply the `PrintXpressTheme` and call `PrintXpressNavGraph`.

**Done when:** The app compiles and launches to a white screen that says "Splash screen."

---

## Phase 2: Auth screens

Copy this entire prompt into Claude Code after Phase 1 is complete.

---

You are helping me build PrintXpress. Read `CLAUDE.md` before doing anything. The project already has its theme (`ui/theme/`), navigation skeleton (`ui/Navigation.kt`), and all screen files stubbed out from Phase 1.

In this phase, build the visual UI for three screens: splash, login, and register. No ViewModels, no Room, no real logic. Use hardcoded strings and `navController.navigate()` calls to move between screens. Everything compiles and looks correct.

The design reference is the UI shown in the PrintXpress design file. Follow the visual rules below exactly.

**Rules for all three screens:**
- White background.
- Use only the named colour tokens from `ui/theme/Color.kt`. No hardcoded hex values anywhere.
- All text in sentence case.
- Rounded corners: `RoundedCornerShape(12.dp)` on buttons and text fields, `RoundedCornerShape(16.dp)` on cards.
- The logo is not available yet. Use a placeholder: a `Box` 80 dp wide and tall, `AccentContainer` background, `RoundedCornerShape(16.dp)`, white `Text` "PX" in `MaterialTheme.typography.titleLarge` centred inside using `contentAlignment = Alignment.Center`. Add a comment: `// TODO: replace with Image(painterResource(R.drawable.logo)) when the logo file is added`.

**SplashScreen:**
- White background, no top bar.
- Centre all content vertically and horizontally using a `Column` with `horizontalAlignment = Alignment.CenterHorizontally` and `verticalArrangement = Arrangement.Center` inside a `Box` that fills the full screen.
- Logo placeholder 180 dp wide and 180 dp tall, centred.
- "PrintXpress" in `MaterialTheme.typography.displaySmall`, `Accent` colour, `Spacer(16.dp)` below the logo.
- "Print, delivered." in `MaterialTheme.typography.bodyMedium`, `TextSecondary`, `Spacer(8.dp)` below the name.
- Use `LaunchedEffect(Unit) { delay(2000); navController.navigate(Screen.Login.route) { popUpTo(Screen.Splash.route) { inclusive = true } } }` to auto-navigate after 2 seconds.

**LoginScreen:**
- White background, no top bar.
- Wrap everything in a `Column` with `modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)`.
- Logo placeholder 80 dp wide and 80 dp tall, centred, with `Spacer(48.dp)` from the top.
- "Welcome back" in `headlineMedium`, `TextPrimary`, `Spacer(24.dp)` below the logo.
- "Sign in to continue" in `bodyMedium`, `TextSecondary`, `Spacer(8.dp)` below the heading.
- Two `OutlinedTextField`s with `modifier = Modifier.fillMaxWidth()` and `shape = RoundedCornerShape(12.dp)`:
  - Label "Email or phone"
  - Label "Password". Use a trailing icon `IconButton` that toggles `passwordVisible` state. When visible, use `Icons.Rounded.Visibility`; when hidden, use `Icons.Rounded.VisibilityOff`. Apply `VisualTransformation.None` or `PasswordVisualTransformation()` accordingly.
- `Spacer(8.dp)`, then "Forgot password?" as a right-aligned `TextButton` with `Modifier.align(Alignment.End)`, label in `Accent`. No action needed yet.
- `Spacer(24.dp)`, then a full-width `Button(modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Accent))` with label "Log in". On click: `navController.navigate(Screen.Home.route)`.
- `Spacer` that pushes the register link to the bottom using `Modifier.weight(1f)` or a fixed spacer of at least 24 dp.
- "Don't have an account?" as plain `Text` in `TextSecondary` followed by a `TextButton` label "Register" in `Accent`. Place them in a `Row` with `horizontalArrangement = Arrangement.Center` and `modifier = Modifier.fillMaxWidth()`. On click: `navController.navigate(Screen.Register.route)`.

**RegisterScreen:**
- White background, no top bar.
- Wrap everything in a `Column` with `modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)`.
- Logo placeholder 80 dp wide and 80 dp tall, centred, with `Spacer(48.dp)` from the top.
- "Create your account" in `headlineMedium`, `TextPrimary`.
- "Join PrintXpress today" in `bodyMedium`, `TextSecondary`, `Spacer(8.dp)` below.
- A `Row` with two `FilterChip`s side by side with `Spacer(8.dp)` between them, wrapped in `Modifier.fillMaxWidth()` with `horizontalArrangement = Arrangement.Center`:
  - Chip "Email": selected when `selectedTab == "email"`.
  - Chip "Phone": selected when `selectedTab == "phone"`.
  - Selected chip: use `FilterChipDefaults.filterChipColors(selectedContainerColor = Accent, selectedLabelColor = OnAccent)`.
  - Unselected chip: use `FilterChipDefaults.filterChipColors(containerColor = AccentContainer, labelColor = Accent)`.
  - On click: update `var selectedTab by remember { mutableStateOf("email") }`.
- Below the chips, show either an "Email" or "Phone" `OutlinedTextField` depending on `selectedTab`. Use `if (selectedTab == "email")` to switch.
- Fields (all `Modifier.fillMaxWidth()`, `RoundedCornerShape(12.dp)`): Full name, then the email/phone field, then Password, then Confirm password.
- Below the Password field, a `Spacer(4.dp)` then a password strength bar:
  ```kotlin
  val strength = when {
      password.length >= 10 -> 1f
      password.length >= 6  -> 0.6f
      else                  -> 0.3f
  }
  val strengthColor = when {
      password.length >= 10 -> Accent
      password.length >= 6  -> Color(0xFFFFA000) // amber
      else                  -> Color(0xFFD32F2F) // red
  }
  LinearProgressIndicator(
      progress = { strength },
      modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
      color = strengthColor,
      trackColor = Divider,
  )
  ```
- `Spacer(24.dp)`, then a full-width `Button` "Create account" in `Accent`, same style as login. On click: `navController.navigate(Screen.Home.route)`.
- `Spacer(24.dp)`, then a `Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center)` with "Already have an account?" plain text and a `TextButton` "Log in" in `Accent`. On click: `navController.popBackStack()`.

**Done when:** All three screens render correctly, the 2-second splash auto-navigation works, and tapping "Register" and "Log in" links moves between screens without crashes.

---

## Phase 3: All remaining UI screens

Copy this entire prompt into Claude Code after Phase 2 is complete.

---

You are helping me build PrintXpress. Read `CLAUDE.md` before doing anything. The theme, navigation, and auth screens are complete from earlier phases.

In this phase, build the visual UI for every remaining screen. No ViewModels, no Room. Use hardcoded sample data defined as local `val` lists inside each composable. Navigation calls use placeholder IDs (e.g. `navController.navigate(Screen.ProductDetail.route + "/1")`).

Use only named colour tokens from `ui/theme/Color.kt`. All corners rounded. Sentence case everywhere. Never hardcode hex values in screen files.

**General rules for all screens:**
- Screen horizontal padding: `padding(horizontal = 16.dp)`.
- Card internal padding: `16.dp`.
- Gap between cards: `12.dp` via `Arrangement.spacedBy(12.dp)`.
- Touch targets at least 48 dp tall.
- Use `Icons.Rounded` for all icons.
- If a layout detail here cannot be achieved with a standard Compose component, use the closest standard component that exists. Do not write custom Layout or Canvas code. Prefer simplicity over perfection.

---

### MainScaffold

Create `app/src/main/java/com/hiranya/printxpress/ui/components/MainScaffold.kt`.

This composable wraps all five bottom-nav destinations. It takes a `navController: NavHostController` and a `content: @Composable (PaddingValues) -> Unit` slot.

The `NavigationBar` has a `Modifier.fillMaxWidth()` and a 1 dp top border drawn with `Modifier.drawBehind { drawLine(color = Divider, start = Offset(0f, 0f), end = Offset(size.width, 0f), strokeWidth = 1.dp.toPx()) }`. No elevation shadow needed beyond that.

Five tabs with `NavigationBarItem`:
- Home: `Icons.Rounded.Home`, label "Home"
- Products: `Icons.Rounded.GridView`, label "Products"
- Orders: `Icons.Rounded.Receipt`, label "Orders"
- Alerts: `Icons.Rounded.Notifications`, label "Alerts"
- Profile: `Icons.Rounded.Person`, label "Profile"

Active item: icon and label in `Accent`. Inactive: icon and label in `TextSecondary`. Use `selectedContentColor = Accent` and `unselectedContentColor = TextSecondary` in `NavigationBarItemDefaults.colors()`.

---

### HomeScreen

Wrap in the `MainScaffold`. The body is a `LazyColumn` with `contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)` and `verticalArrangement = Arrangement.spacedBy(24.dp)`.

**Top bar** (not a TopAppBar composable, just a `Row` item in the LazyColumn):
- Left: logo placeholder `Box` 32 dp tall, 80 dp wide, `AccentContainer` background, `RoundedCornerShape(8.dp)`, `Text` "PX" in `Accent` centred using `contentAlignment = Alignment.Center`.
- Right: `IconButton` with `Icons.Rounded.Notifications`. Show a small 8 dp `Box` badge with `AccentContainer` background filled circle overlaid at the top-right of the icon when `hasUnread = true` (hardcode `true`). Use a `Box(contentAlignment = Alignment.TopEnd)` wrapping the icon to position the badge dot.

**Greeting** (`Column`, `verticalArrangement = Arrangement.spacedBy(4.dp)`):
- "Good morning, Sarah" in `titleLarge`, `TextPrimary`.
- "What would you like to print today?" in `bodyMedium`, `TextSecondary`.

**Search bar**:
```kotlin
OutlinedTextField(
    value = "",
    onValueChange = {},
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(50.dp), // pill shape
    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Search", tint = TextSecondary) },
    placeholder = { Text("Search products...", color = TextDisabled) },
    singleLine = true,
)
```

**Offers section** (use `item` in the LazyColumn):
- Heading "Current offers" in `titleMedium`, `TextPrimary`.
- `Spacer(8.dp)`.
- Hardcode two sample offers as a data class `data class Offer(val title: String, val code: String, val expiry: String, val description: String)` defined locally.
- `LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp))`:
  - Each offer card: `Card(modifier = Modifier.width(280.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = AccentContainer), border = BorderStroke(0.dp, Color.Transparent))`.
  - Inside: `Row` so you can draw the left accent line. Use `Box(modifier = Modifier.width(4.dp).fillMaxHeight().background(Accent))` as the first child, then a `Column(modifier = Modifier.padding(16.dp))` with title in `titleMedium`, description in `bodyMedium TextSecondary`, the code in a small `Surface(shape = RoundedCornerShape(50.dp), color = Accent)` pill with white `labelSmall` text, and expiry in `labelSmall TextSecondary`.

**Categories section** (use `item` for the heading, then `items` with the grid):
- Heading "Browse by category" in `titleMedium`, `TextPrimary`.
- `Spacer(8.dp)`.
- Because `LazyVerticalGrid` cannot be nested inside `LazyColumn`, use a plain `Column` with two children per row, using a `LazyColumn` item with a manual two-column grid built from a chunked list:
  ```kotlin
  val chunked = categories.chunked(2)
  chunked.forEach { pair ->
      Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
          pair.forEach { category ->
              CategoryCard(category, modifier = Modifier.weight(1f)) { navController.navigate(...) }
          }
          if (pair.size == 1) Spacer(Modifier.weight(1f))
      }
      Spacer(Modifier.height(12.dp))
  }
  ```
- Each category card: `Card(modifier = modifier, shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, Divider), colors = CardDefaults.cardColors(containerColor = Background))`. Inside: a `Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally)`:
  - `Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center)` with `Box(modifier = Modifier.matchParentSize().background(AccentContainer, RoundedCornerShape(12.dp)))` and the icon on top: `Icon(icon, tint = Accent, modifier = Modifier.size(28.dp))`.
  - `Spacer(8.dp)`.
  - Category name in `bodyLarge`, `TextPrimary`, `textAlign = TextAlign.Center`.
- Hardcode these 7 categories with appropriate `Icons.Rounded` icons:
  - Business cards: `Icons.Rounded.Badge`
  - Posters: `Icons.Rounded.Image`
  - Banners: `Icons.Rounded.Panorama`
  - Flyers: `Icons.Rounded.Article`
  - Stickers: `Icons.Rounded.Stars`
  - Mugs: `Icons.Rounded.LocalCafe`
  - T-shirts: `Icons.Rounded.Checkroom`

---

### ProductListScreen (receives a categoryId: Long)

Use a `Scaffold` with `TopAppBar` (back arrow, category name as title, use the hardcoded list to pick the name from the categoryId).

Body content in a `Column`:
- Filter row: `Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically)`:
  - `OutlinedTextField(modifier = Modifier.weight(1f), ...)` with label "Search products...", pill shape.
  - `IconButton` with `Icons.Rounded.FilterList`.
- Product grid built the same way as the category grid (manual chunked two-column layout) inside a `LazyColumn`.
- Each product card: `Card(shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, Divider))`. Inside `Column(modifier = Modifier.padding(8.dp))`:
  - `Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f), contentAlignment = Alignment.Center)` with `AccentContainer` background and the category icon in `Accent` at 40 dp.
  - `Spacer(8.dp)`.
  - Product name in `bodyLarge`, `TextPrimary`.
  - "From LKR 350" in `bodyMedium`, `Accent`.
  - `Spacer(8.dp)`.
  - `OutlinedButton(onClick = { navController.navigate(...) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(50.dp), border = BorderStroke(1.dp, Accent))` with label "Order" in `Accent`.
- Hardcode 4 sample products.

---

### ProductDetailScreen (receives a productId: Long)

Use a `Scaffold` with `TopAppBar` (back arrow, product name as title).

The body is a `Column` with `verticalScroll(rememberScrollState())`. At the bottom of the screen, use a sticky bottom bar by placing it outside the scroll column inside the `Scaffold` body using `bottomBar`.

**Scroll column content** (`padding(bottom = 80.dp)` to avoid the sticky bar overlapping):
- `Box(modifier = Modifier.fillMaxWidth().height(220.dp), contentAlignment = Alignment.Center)` with `AccentContainer` background and a large icon in `Accent` at 64 dp.
- `Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp))`:
  - Product name in `titleLarge`, `TextPrimary`.
  - "LKR 350" in `titleLarge`, `Accent`.
  - Description in `bodyMedium`, `TextSecondary`.
- Divider.
- "Choose your options" in `titleMedium`, `TextPrimary`, with `padding(horizontal = 16.dp, vertical = 8.dp)`.
- Size chips row: `LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp))` with `FilterChip` for each of A6, A5, A4, A3. Selected: `containerColor = Accent`, `labelColor = OnAccent`. Unselected: `containerColor = Background`, border `Divider`.
- Material chips row: same style for Matte, Glossy, Recycled.
- Quantity row: `Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically)`:
  - `Text("Quantity", style = bodyLarge, color = TextPrimary)`.
  - `Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp))`:
    - `OutlinedIconButton(onClick = { if (qty > 1) qty-- }, border = BorderStroke(1.dp, Accent))` with `Icons.Rounded.Remove`.
    - `Text("$qty", style = titleMedium, color = TextPrimary)`.
    - `OutlinedIconButton(onClick = { qty++ }, border = BorderStroke(1.dp, Accent))` with `Icons.Rounded.Add`.
  - If `OutlinedIconButton` is not available in the installed Compose version, use `OutlinedButton` with padding reduced.
- Design tab: `TabRow(selectedTabIndex = selectedTab, containerColor = Background, contentColor = Accent)`:
  - Tab "Upload file".
  - Tab "Enter text".
- Tab content:
  - Upload tab: `Box(modifier = Modifier.fillMaxWidth().height(120.dp).padding(horizontal = 16.dp).border(width = 1.dp, color = Accent, shape = RoundedCornerShape(12.dp)).background(AccentContainer, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center)` with a `Column(horizontalAlignment = Alignment.CenterHorizontally)` containing `Icon(Icons.Rounded.Upload, tint = Accent, modifier = Modifier.size(32.dp))` and `Text("Tap to upload your design", style = bodyMedium, color = TextSecondary)`.
  - Text tab: `OutlinedTextField(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(120.dp), placeholder = { Text("Type your custom text here...") }, maxLines = 5)`.

**Sticky bottom bar** (as `bottomBar` in Scaffold):
```kotlin
Surface(shadowElevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("LKR 350", style = titleLarge, color = Accent)
        Button(
            onClick = { navController.navigate(Screen.PlaceOrder.route + "/1") },
            modifier = Modifier.height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Accent),
        ) {
            Text("Add to order", style = labelMedium, color = OnAccent)
        }
    }
}
```

---

### PlaceOrderScreen (receives a productId: Long)

Use a `Scaffold` with `TopAppBar` ("Place order", back arrow).

`var currentStep by remember { mutableStateOf(1) }`.

**Step indicator** (always visible at the top of the body):
```kotlin
Row(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
) {
    // For each step 1, 2, 3:
    // Done step (step < currentStep): Box 36 dp circle, AccentContainer background,
    //   Icon(Icons.Rounded.Check, tint = Accent, modifier = Modifier.size(18.dp)) centred.
    // Active step (step == currentStep): Box 36 dp circle, Accent background,
    //   Text("$step", color = OnAccent) centred.
    // Pending step (step > currentStep): Box 36 dp circle, Background with border Divider,
    //   Text("$step", color = TextDisabled) centred.
    // Between each circle: HorizontalDivider(modifier = Modifier.weight(1f), color = Divider)
}
```

Body is a `Column` with `verticalScroll(rememberScrollState()).padding(16.dp)`.

**Step 1: Order summary**:
- `Card(shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, Divider), modifier = Modifier.fillMaxWidth())` with a `Column(padding = 16.dp)`:
  - Product name in `titleMedium`, `TextPrimary`.
  - "Size: A4 · Material: Matte · Qty: 1" in `bodyMedium`, `TextSecondary`.
  - "Design: Custom text" in `bodyMedium`, `TextSecondary`.
  - `Divider`.
  - `Row(horizontalArrangement = Arrangement.SpaceBetween)`: "Subtotal" in `bodyLarge`, "LKR 350" in `titleMedium Accent`.
- `Spacer(16.dp)`.
- `Button(onClick = { currentStep = 2 }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Accent))` label "Next".

**Step 2: Delivery**:
- "How would you like to receive your order?" in `titleMedium`, `TextPrimary`.
- `var deliveryMethod by remember { mutableStateOf("pickup") }`.
- `Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth())`:
  - Two `Card`s each `Modifier.weight(1f)`, `shape = RoundedCornerShape(16.dp)`. Selected: `containerColor = AccentContainer`, border `BorderStroke(2.dp, Accent)`. Unselected: `containerColor = Background`, border `BorderStroke(1.dp, Divider)`.
  - Each card has `Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp))` with the icon and label. "Pickup": `Icons.Rounded.Store`. "Delivery": `Icons.Rounded.LocalShipping`.
  - `clickable { deliveryMethod = "pickup" }` or `"delivery"`.
- If `deliveryMethod == "delivery"`:
  - `Spacer(16.dp)`.
  - "Delivery address" in `titleMedium`, `TextPrimary`.
  - Two hardcoded address rows, each a `Card(modifier = Modifier.fillMaxWidth())`. Selected: `AccentContainer` background, 4 dp `Accent` left border using `Modifier.border(start = 4.dp, ...)` — if that modifier doesn't exist, just use `borderStroke = BorderStroke(2.dp, Accent)` on the card.
  - "+ Add new address" `TextButton` in `Accent` with `Icons.Rounded.Add` leading icon.
- `Button(onClick = { currentStep = 3 }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Accent))` label "Next".

**Step 3: Confirm**:
- `Card(shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, Divider), modifier = Modifier.fillMaxWidth())`:
  - "1 item" in `bodyLarge`.
  - "Delivery: Pickup" in `bodyMedium TextSecondary`.
  - Divider.
  - Subtotal row, delivery fee row (LKR 300), total row with `titleLarge Accent`.
- `Spacer(16.dp)`.
- `Button(onClick = { navController.navigate(Screen.OrderConfirmation.route + "/1042") }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Accent))` label "Place order".

---

### OrderConfirmationScreen

No bottom nav. A `Scaffold` with no top bar. Body is a `Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center)`:
- `Box(modifier = Modifier.size(96.dp), contentAlignment = Alignment.Center)` with `Box(modifier = Modifier.matchParentSize().background(AccentContainer, CircleShape))` and `Icon(Icons.Rounded.CheckCircle, tint = Accent, modifier = Modifier.size(48.dp))`.
- `Spacer(16.dp)`.
- "Order placed!" in `headlineMedium`, `TextPrimary`, centred.
- `Spacer(8.dp)`.
- "Your order is being processed. We'll notify you when it's ready." in `bodyMedium`, `TextSecondary`, `textAlign = TextAlign.Center`.
- `Spacer(16.dp)`.
- `Surface(shape = RoundedCornerShape(50.dp), color = AccentContainer)` containing `Text("Order #1042", style = labelMedium, color = Accent, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))`.
- `Spacer(24.dp)`.
- `Button(onClick = { navController.navigate(Screen.Orders.route) }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Accent))` label "View my orders".
- `Spacer(12.dp)`.
- `OutlinedButton(onClick = { navController.navigate(Screen.Home.route) }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Accent))` with `Text("Back to home", color = Accent)`.

---

### OrdersScreen

Wrap in `MainScaffold`. Use a `Scaffold` with `TopAppBar` title "My orders".

`var selectedTab by remember { mutableStateOf(0) }`.
`val tabs = listOf("All", "Active", "Completed", "Cancelled")`.

Body:
- `TabRow(selectedTabIndex = selectedTab, containerColor = Background, contentColor = Accent)`: one `Tab` per entry in `tabs`.
- `LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp))`:
  - Hardcode 3 sample orders across different statuses.
  - Each order card: `Card(shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, Divider), modifier = Modifier.fillMaxWidth())`. Inside `Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp))`:
    - `Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically)`: order number in `bodyLarge TextPrimary`, status badge `Surface(shape = RoundedCornerShape(50.dp), color = badgeContainerColor)` with `Text(status, color = badgeTextColor, style = labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))`.
    - `Text("15 May 2025 · 2 items", style = labelSmall, color = TextSecondary)`.
    - `Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically)`: `Text("LKR 1,200", style = titleMedium, color = Accent)`, `TextButton(onClick = { navController.navigate(Screen.OrderDetail.route + "/1042") })` label "View details" in `Accent`.
  - Status badge colours: Processing: `AccentContainer`/`Accent`. Printing: `Color(0xFFFFF8E1)`/`Color(0xFFF57F17)`. Ready for pickup: `Color(0xFFE8F5E9)`/`Color(0xFF2E7D32)`. Out for delivery: `Color(0xFFE3F2FD)`/`Color(0xFF1565C0)`. Completed: `Color(0xFFF5F5F5)`/`TextSecondary`. Cancelled: `Color(0xFFFFEBEE)`/`Color(0xFFC62828)`.

---

### OrderDetailScreen (receives an orderId: Long)

`Scaffold` with `TopAppBar` title "Order #1042", back arrow.

Body: `LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp))`.

**Status stepper**:
```kotlin
val stages = listOf("Processing", "Printing", "Ready", "Delivering", "Completed")
val currentStage = 1 // hardcode index
Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
    stages.forEachIndexed { index, stage ->
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) {
                val bgColor = if (index <= currentStage) Accent else Divider
                Box(modifier = Modifier.matchParentSize().background(bgColor, CircleShape))
                val icon = when {
                    index < currentStage  -> Icons.Rounded.Check
                    index == currentStage -> Icons.Rounded.Schedule
                    else                  -> Icons.Rounded.RadioButtonUnchecked
                }
                Icon(icon, contentDescription = stage, tint = if (index <= currentStage) OnAccent else TextDisabled, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.height(4.dp))
            Text(stage, style = labelSmall, color = if (index <= currentStage) Accent else TextDisabled, textAlign = TextAlign.Center)
        }
        if (index < stages.lastIndex) HorizontalDivider(modifier = Modifier.weight(0.5f), color = if (index < currentStage) Accent else Divider)
    }
}
```

**Items section**: heading "Items in this order" in `titleMedium`. One `Card` per item (hardcode 1 item).

**Delivery section**: heading "Delivery details" in `titleMedium`. `Text("Pickup from store", style = bodyMedium, color = TextSecondary)`.

**Totals section**: `Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth())` with subtotal, delivery fee, and total rows.

**Action row** (hardcode status as Processing so the buttons show):
- `Row(horizontalArrangement = Arrangement.spacedBy(12.dp))`:
  - `OutlinedButton(modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color(0xFFC62828)))` label `Text("Cancel order", color = Color(0xFFC62828))`.
  - `OutlinedButton(modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Accent))` label `Text("Reschedule", color = Accent)`.

---

### NotificationsScreen

Wrap in `MainScaffold`. Use a `Scaffold` with `TopAppBar`:
- Title "Notifications".
- `actions = { TextButton(onClick = {}) { Text("Mark all read", color = Accent) } }`.

`LazyColumn(contentPadding = PaddingValues(vertical = 8.dp))`:

Hardcode 3 notifications (2 unread, 1 read). Each notification row:
- Unread: `Row(modifier = Modifier.fillMaxWidth().background(AccentContainer))`. Inside use `Box(modifier = Modifier.width(4.dp).fillMaxHeight().background(Accent))` as a left-border effect by wrapping the entire row in a `Row` where the first item is that 4 dp box.
- Read: `Row(modifier = Modifier.fillMaxWidth().background(Background))`.
- Inside the row (after the optional 4 dp border): `Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top)`:
  - Leading icon: `Icon(icon, tint = Accent, modifier = Modifier.size(24.dp).padding(top = 2.dp))`.
  - `Column(modifier = Modifier.weight(1f))`:
    - Title in `bodyLarge`, `TextPrimary`, `fontWeight = if (unread) FontWeight.SemiBold else FontWeight.Normal`.
    - Message in `bodyMedium`, `TextSecondary`.
    - Timestamp in `labelSmall`, `TextDisabled`.
- `HorizontalDivider(color = Divider)`.

---

### ProfileScreen

Wrap in `MainScaffold`. Use a `Scaffold` with `TopAppBar`:
- Title "Profile".
- `actions = { IconButton(onClick = {}) { Icon(Icons.Rounded.Settings, tint = TextSecondary) } }`.

Body: `LazyColumn(contentPadding = PaddingValues(vertical = 16.dp))`.

**Avatar section** (`Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(16.dp))`):
- `Box(modifier = Modifier.size(72.dp), contentAlignment = Alignment.Center)` with `Box(modifier = Modifier.matchParentSize().background(AccentContainer, CircleShape))` and `Text("S", style = titleLarge, color = Accent)`.
- `Spacer(8.dp)`.
- "Sarah Johnson" in `titleLarge`, `TextPrimary`.
- "sarah@example.com" in `bodyMedium`, `TextSecondary`.
- `TextButton(onClick = { navController.navigate(Screen.EditProfile.route) })` label "Edit profile" in `Accent`.

**Menu rows** (use `HorizontalDivider` before the first row):
```kotlin
data class MenuItem(val icon: ImageVector, val label: String, val route: String?)
val items = listOf(
    MenuItem(Icons.Rounded.LocationOn, "Delivery addresses", Screen.Addresses.route),
    MenuItem(Icons.Rounded.FolderOpen, "Saved designs", null),
    MenuItem(Icons.Rounded.Print, "Print guidelines", Screen.PrintGuidelines.route),
    MenuItem(Icons.Rounded.HelpOutline, "FAQs and support", Screen.Faq.route),
    MenuItem(Icons.Rounded.Info, "About PrintXpress", null),
)
```
Each row: `ListItem(headlineContent = { Text(item.label, style = bodyLarge, color = TextPrimary) }, leadingContent = { Icon(item.icon, tint = Accent) }, trailingContent = { Icon(Icons.Rounded.ChevronRight, tint = TextSecondary) }, modifier = Modifier.clickable { item.route?.let { navController.navigate(it) } })`. Follow each with `HorizontalDivider(color = Divider)`.

**Log out row**: `ListItem(headlineContent = { Text("Log out", style = bodyLarge, color = Color(0xFFC62828)) }, leadingContent = { Icon(Icons.Rounded.Logout, tint = Color(0xFFC62828)) }, modifier = Modifier.clickable { navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } } })`.

---

### EditProfileScreen

`Scaffold` with `TopAppBar`:
- Title "Edit profile".
- Back arrow.
- `actions = { TextButton(onClick = {}) { Text("Save", color = Accent) } }`.

Body: `Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp))`:
- Three `OutlinedTextField`s (`fillMaxWidth()`, `RoundedCornerShape(12.dp)`): Full name, Email, Phone.
- `Spacer(8.dp)`.
- `HorizontalDivider(color = Divider)`.
- `Spacer(8.dp)`.
- `Text("Change password", style = titleMedium, color = TextPrimary)`.
- Three more `OutlinedTextField`s: Current password, New password, Confirm new password (all with `PasswordVisualTransformation()`).

---

### AddressesScreen

`Scaffold` with `TopAppBar` title "Delivery addresses", back arrow.

`LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp))`:

Hardcode 2 addresses. Each address card: `Card(shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, Divider), modifier = Modifier.fillMaxWidth())`. Inside `Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp))`:
- Address text in `bodyLarge`, `TextPrimary`.
- If default: `Surface(shape = RoundedCornerShape(50.dp), color = AccentContainer)` with `Text("Default", style = labelSmall, color = Accent, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))`.
- `HorizontalDivider(color = Divider)`.
- `Row(horizontalArrangement = Arrangement.SpaceBetween)`:
  - `Row(horizontalArrangement = Arrangement.spacedBy(16.dp))`: `TextButton` "Edit" in `TextSecondary`, `TextButton` "Delete" in `TextSecondary`.
  - If not default: `TextButton` "Set as default" in `Accent`.

"Add new address" card at the bottom: `Card(shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, Accent), modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Background))`. Inside `Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally))`: `Icon(Icons.Rounded.Add, tint = Accent)`, `Text("Add new address", style = bodyLarge, color = Accent)`.

---

### PrintGuidelinesScreen

`Scaffold` with `TopAppBar` title "Print guidelines", back arrow.

Hardcode 5 topics as `data class GuidelineItem(val title: String, val body: String)`. Add 2 to 3 sentences of real guidance per item:
- **Accepted file types**: "We accept PDF, PNG, and JPG files. PDF is preferred for print because it preserves fonts, colours, and layout exactly. PNG works well for logos and artwork with transparent backgrounds."
- **Resolution requirements**: "Files must be at least 300 DPI for sharp print results. Images from websites are usually only 72 DPI and will print blurry. Always export at 300 DPI or higher from your design tool."
- **Colour mode**: "Set your file to CMYK colour mode before exporting. RGB files will be converted automatically but the printed colours may look slightly different. Bright neon or screen colours often shift when printed."
- **Bleed and margins**: "Add a 3 mm bleed on all sides so the design extends past the cut line. Keep important text and logos at least 5 mm from the edge. This prevents accidental cropping when the sheet is trimmed."
- **How to prepare your file**: "Flatten all layers and embed all linked images before saving. Outline any text that uses custom fonts so the print file is self-contained. Save as PDF/X-1a for the best compatibility."

`LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp))`:

For each item, use `var expanded by remember { mutableStateOf(false) }` per item (use a `remember` keyed list or define inside the `items` block):
```kotlin
items(guidelines) { item ->
    var expanded by remember { mutableStateOf(false) }
    Card(shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Divider), modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(item.title, style = bodyLarge, color = TextPrimary, modifier = Modifier.weight(1f))
                val rotation by animateFloatAsState(if (expanded) 180f else 0f, label = "chevron")
                Icon(Icons.Rounded.ExpandMore, tint = Accent, modifier = Modifier.rotate(rotation))
            }
            if (expanded) {
                HorizontalDivider(color = Divider)
                Text(
                    item.body,
                    style = bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.fillMaxWidth().background(AccentContainer).padding(16.dp),
                )
            }
        }
    }
}
```

---

### FaqScreen

`Scaffold` with `TopAppBar` title "FAQs and support", back arrow.

**Search bar** at the top (just below the app bar, not inside LazyColumn header — use a `Column` wrapping a search field and then a `LazyColumn`):
```kotlin
Column(modifier = Modifier.fillMaxSize()) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(50.dp),
        leadingIcon = { Icon(Icons.Rounded.Search, tint = TextSecondary) },
        placeholder = { Text("Search FAQs...", color = TextDisabled) },
        singleLine = true,
    )
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // FAQ items...
        // Contact us card...
    }
}
```

This ensures the search bar placeholder text is vertically centred inside the field (the default `OutlinedTextField` behaviour). Do not add manual vertical padding to the placeholder text. Do not wrap the search bar in a `Box` with manual alignment.

Hardcode 5 FAQ items using the same `GuidelineItem` data class and the same expandable accordion pattern as `PrintGuidelinesScreen`:
- "How long does printing take?" — "Standard orders are usually ready within 1 to 2 business days. Express orders are ready the same day if placed before noon. We'll send you a notification when your order is ready."
- "What file formats do you accept?" — "We accept PDF, PNG, and JPG. PDF is the best choice for printed documents. PNG is ideal for logos and designs with transparent backgrounds."
- "Can I cancel or change my order?" — "You can cancel or make changes while the order is still in Processing status. Once printing starts, changes are not possible. Open your order in the app and tap Cancel."
- "How do I get my order?" — "You can pick it up from our store at no extra cost, or choose delivery for a flat LKR 300 fee. Delivery usually arrives the next business day."
- "What payment methods do you accept?" — "We accept cash on pickup and cash on delivery. Online payment support is coming soon."

**"Contact us" card** at the bottom of the LazyColumn:
```kotlin
item {
    Spacer(Modifier.height(8.dp))
    Text("Contact us", style = titleMedium, color = TextPrimary)
    Spacer(Modifier.height(8.dp))
    Card(shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, Divider), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Rounded.Phone, tint = Accent)
                Text("+94 77 123 4567", style = bodyLarge, color = TextPrimary)
            }
            HorizontalDivider(color = Divider)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Rounded.Email, tint = Accent)
                Text("hello@printxpress.lk", style = bodyLarge, color = TextPrimary)
            }
        }
    }
}
```

**Done when:** All screens render without crashing, navigation between them works, and the bottom nav switches between the five main tabs correctly. Pay special attention to the FAQ screen: confirm the search bar text is vertically centred and the search field does not overflow the screen width.

---

## Phase 4: Database layer

Copy this entire prompt into Claude Code after Phase 3 is complete.

---

You are helping me build PrintXpress. Read `CLAUDE.md` before doing anything. The full UI is built. Now build the database layer. No ViewModels yet. No changes to any screen files.

Reference `docs/04-database-design.md` for the exact schema (tables, columns, types, keys, and relationships).

**Ask me to approve the Room and KSP versions before writing any code if they were not already added in Phase 1. Wait for my reply.**

Create all files inside `app/src/main/java/com/hiranya/printxpress/data/`.

**Step 1: Entities**

Create one Kotlin data class per table, annotated with `@Entity`. Follow the schema in `04-database-design.md` exactly.

Tables: `users`, `addresses`, `categories`, `products`, `orders`, `order_items`, `saved_designs`, `notifications`, `promotions`.

Add `@ForeignKey` annotations where the schema specifies a foreign key, with the correct `onDelete` behaviour (CASCADE or SET_NULL as documented).

**Step 2: DAOs**

Create one `@Dao` interface per entity. Each DAO needs at minimum:

- `users`: insert, findByEmail, findByPhone, findById, update.
- `addresses`: insert, update, delete, getByUser, setDefault (update isDefault for all rows of a user then set the chosen one true).
- `categories`: insert, getAll.
- `products`: insert, getByCategory, getById, getAll.
- `orders`: insert, update, getByUser, getById.
- `order_items`: insert, getByOrder.
- `saved_designs`: insert, delete, getByUser.
- `notifications`: insert, update (mark read), getByUser, markAllRead.
- `promotions`: insert, getActive (WHERE validTo >= currentTime AND validFrom <= currentTime).

Use `@Query`, `@Insert`, `@Update`, `@Delete` annotations. All functions are `suspend` except those returning `Flow`.

**Step 3: Database class**

Create `PrintXpressDatabase.kt`. It is a `@Database` listing all entities and version 1.

Add a `RoomDatabase.Callback` that seeds the database on first creation. The `onCreate` callback should insert:

Categories (all 7): Business cards, Posters, Banners, Flyers, Stickers, Mugs, T-shirts. Give each a short description.

Products (at least 2 per category, 14 minimum): realistic names, descriptions, and prices in LKR. Example for business cards: "Standard business card" at LKR 350 with sizeOptions "85x55mm" and materialOptions "Matte,Glossy,Kraft".

Promotions (2 sample offers): one active, one expired. Set validFrom and validTo as epoch milliseconds.

Use a `companion object` with a `INSTANCE` and `getDatabase(context)` method using `Room.databaseBuilder`. Use `.fallbackToDestructiveMigration()` for simplicity.

**Step 4: Repositories**

Create one repository class per feature area in `data/repository/`:

- `UserRepository(private val userDao: UserDao)`: register, login, updateProfile.
- `ProductRepository(private val categoryDao: CategoryDao, private val productDao: ProductDao)`: getCategories, getProductsByCategory, getProductById.
- `OrderRepository(private val orderDao: OrderDao, private val orderItemDao: OrderItemDao, private val notificationDao: NotificationDao)`: placeOrder (inserts the order, its items, and a confirmation notification in one function), getOrdersByUser, cancelOrder (updates status to cancelled), getOrderById.
- `NotificationRepository(private val notificationDao: NotificationDao)`: getByUser, markRead, markAllRead.
- `AddressRepository(private val addressDao: AddressDao)`: getByUser, add, update, delete, setDefault.
- `PromotionRepository(private val promotionDao: PromotionDao)`: getActive.

**Step 5: Password hashing**

In `data/util/HashUtil.kt`, add a function `fun hashPassword(password: String): String` that returns a SHA-256 hex digest. Use `java.security.MessageDigest`, no external library.

Add a matching `fun checkPassword(input: String, hash: String): Boolean` that hashes the input and compares.

**Done when:** The project compiles with no errors. The database layer exists and all repositories are importable. No screens have changed.

---

## Phase 5: Auth and product logic

Copy this entire prompt into Claude Code after Phase 4 is complete.

---

You are helping me build PrintXpress. Read `CLAUDE.md` before doing anything. The UI and database layer are complete. Now wire auth and product browsing to real data.

Create ViewModels in `app/src/main/java/com/hiranya/printxpress/viewmodel/`.

**Step 1: Session management**

Create `data/util/SessionManager.kt`. It holds a singleton `var loggedInUserId: Long? = null` in memory. Add `fun login(userId: Long)`, `fun logout()`, and `fun isLoggedIn(): Boolean`. This is simple in-memory state, no SharedPreferences yet.

Update `SplashScreen.kt`: after the 2-second delay, if `SessionManager.isLoggedIn()` navigate to `HomeScreen`, otherwise navigate to `LoginScreen`.

**Step 2: AuthViewModel**

Create `AuthViewModel(application: Application) : AndroidViewModel(application)`.

Expose:
- `val loginError: MutableState<String?>`
- `val registerError: MutableState<String?>`
- `val isLoading: MutableState<Boolean>`

Functions:
- `fun login(emailOrPhone: String, password: String, onSuccess: () -> Unit)`: launch a coroutine, query `UserRepository`, hash the input with `HashUtil`, compare with the stored hash, call `SessionManager.login()` and `onSuccess()` on match, or set `loginError` on failure.
- `fun register(fullName: String, emailOrPhone: String, isEmail: Boolean, password: String, confirmPassword: String, onSuccess: () -> Unit)`: validate inputs (name at least 2 chars, password at least 6 chars, passwords match), check for duplicate email or phone, hash the password, insert the user, call `SessionManager.login()` and `onSuccess()`.

Update `LoginScreen.kt`:
- Add `val viewModel: AuthViewModel = viewModel()`.
- Replace the hardcoded `navController.navigate(Screen.Home.route)` with a call to `viewModel.login(...)`.
- Show a `CircularProgressIndicator` inside the button when `isLoading` is true.
- Show `loginError` as a `Text` in red (`Color(0xFFC62828)`) in `labelSmall` below the button if it is not null.

Update `RegisterScreen.kt` the same way using `viewModel.register(...)`.

**Step 3: HomeViewModel**

Create `HomeViewModel(application: Application) : AndroidViewModel(application)`.

Expose:
- `val categories: State<List<Category>>` loaded from `ProductRepository.getCategories()` in `init {}`.
- `val activePromotions: State<List<Promotion>>` loaded from `PromotionRepository.getActive()`.
- `val unreadCount: State<Int>` loaded from `NotificationRepository.getByUser(SessionManager.loggedInUserId!!)` counting unread.

Update `HomeScreen.kt`:
- Add `val viewModel: HomeViewModel = viewModel()`.
- Replace the hardcoded category list with `viewModel.categories.value`.
- Replace the hardcoded offers with `viewModel.activePromotions.value`. Show the offers row only when the list is not empty.
- Show the badge dot on the bell icon when `viewModel.unreadCount.value > 0`.

**Step 4: ProductViewModel**

Create `ProductViewModel(application: Application) : AndroidViewModel(application)`.

Expose:
- `val products: State<List<Product>>`
- `val selectedProduct: State<Product?>`
- `val searchQuery: MutableState<String>`

Functions:
- `fun loadProductsByCategory(categoryId: Long)`: loads products and filters by `searchQuery`.
- `fun loadProduct(productId: Long)`: loads a single product into `selectedProduct`.

Update `ProductListScreen.kt` to use `viewModel.loadProductsByCategory(categoryId)` and show `viewModel.products.value`. Connect the search field to `viewModel.searchQuery`.

Update `ProductDetailScreen.kt` to use `viewModel.loadProduct(productId)` and show real product name, price, description, and option chips from `sizeOptions.split(",")` and `materialOptions.split(",")`.

**Done when:** Login and register connect to the real database. Incorrect credentials show an error. Registering twice with the same email shows a duplicate error. The home screen loads real categories and promotions from Room. The product list shows real products from Room and the search field filters the list.

---

## Phase 6: Orders, notifications, and profile logic

Copy this entire prompt into Claude Code after Phase 5 is complete.

---

You are helping me build PrintXpress. Read `CLAUDE.md` before doing anything. Auth and product browsing are wired to Room. Now connect the remaining features: placing orders, tracking orders, notifications, and profile management.

**Step 1: OrderViewModel**

Create `OrderViewModel(application: Application) : AndroidViewModel(application)`.

Expose:
- `val orders: State<List<Order>>`
- `val selectedOrder: State<Order?>`
- `val orderItems: State<List<OrderItem>>`
- `val isLoading: MutableState<Boolean>`
- `val error: MutableState<String?>`

Functions:
- `fun loadOrders()`: load all orders for `SessionManager.loggedInUserId`.
- `fun loadOrder(orderId: Long)`: load a single order and its items.
- `fun placeOrder(productId: Long, quantity: Int, size: String, material: String, designPath: String?, customText: String?, fulfilment: String, addressId: Long?, onSuccess: (orderId: Long) -> Unit)`: call `OrderRepository.placeOrder(...)`, then call `onSuccess` with the new orderId.
- `fun cancelOrder(orderId: Long)`: call `OrderRepository.cancelOrder(orderId)`, then reload orders.

Update `PlaceOrderScreen.kt`:
- Add `val viewModel: OrderViewModel = viewModel()`.
- On "Place order" tap, call `viewModel.placeOrder(...)` with the chosen specs. Pass the real `productId` from the nav argument. On success, navigate to `OrderConfirmationScreen` with the real `orderId`.
- Show a `CircularProgressIndicator` on the button while loading.

Update `OrderConfirmationScreen.kt`: show the real order number from the nav argument.

Update `OrdersScreen.kt`:
- Add `val viewModel: OrderViewModel = viewModel()`.
- Call `viewModel.loadOrders()` in a `LaunchedEffect`. Show `viewModel.orders.value`.
- The tab filter (All / Active / Completed / Cancelled) filters the list locally. Active means status is processing, printing, ready for pickup, or out for delivery.
- If the list for the chosen tab is empty, show a centred empty state inside the `LazyColumn`: a large `Icon(Icons.Rounded.Receipt, tint = AccentContainer, modifier = Modifier.size(80.dp))`, `Text("No orders here", style = titleMedium, color = TextPrimary)`, and if the tab is "All" add a `Button` "Browse products" in `Accent` that navigates to the Products tab.

Update `OrderDetailScreen.kt`: call `viewModel.loadOrder(orderId)` and show real data. Wire the cancel button to `viewModel.cancelOrder(orderId)`.

**Step 2: NotificationsViewModel**

Create `NotificationsViewModel(application: Application) : AndroidViewModel(application)`.

Expose:
- `val notifications: State<List<Notification>>`

Functions:
- `fun load()`: load notifications for the logged-in user, most recent first.
- `fun markRead(notificationId: Long)`: update the notification and reload.
- `fun markAllRead()`: call `NotificationRepository.markAllRead(userId)` and reload.

Update `NotificationsScreen.kt`:
- Call `viewModel.load()` on entry.
- Show real notifications.
- Tapping a row calls `viewModel.markRead(notificationId)` and updates the row background.
- "Mark all read" calls `viewModel.markAllRead()`.

**Step 3: ProfileViewModel**

Create `ProfileViewModel(application: Application) : AndroidViewModel(application)`.

Expose:
- `val user: State<User?>`
- `val addresses: State<List<Address>>`
- `val savedDesigns: State<List<SavedDesign>>`
- `val updateError: MutableState<String?>`
- `val updateSuccess: MutableState<Boolean>`

Functions:
- `fun load()`: load the current user and their addresses.
- `fun updateProfile(fullName: String, email: String?, phone: String?)`: validate, check for duplicates, update, reload.
- `fun changePassword(current: String, new: String, confirm: String)`: hash current and check against stored hash, validate new password length, hash new and update.
- `fun addAddress(label: String, line1: String, city: String, postalCode: String)`: insert and reload.
- `fun deleteAddress(addressId: Long)`: delete and reload.
- `fun setDefaultAddress(addressId: Long)`: call `AddressRepository.setDefault` and reload.
- `fun logout()`: call `SessionManager.logout()`.

Update `ProfileScreen.kt`: show the real user name and email from `viewModel.user.value`. Wire the "Log out" row to `viewModel.logout()` then navigate to `LoginScreen` clearing the back stack.

Update `EditProfileScreen.kt`: pre-fill fields from `viewModel.user.value`. Wire "Save" to `viewModel.updateProfile(...)`. Show `updateError` as red text if not null. On `updateSuccess`, pop back.

Update `AddressesScreen.kt`: show `viewModel.addresses.value`. Wire delete, set default, and add new address to the ViewModel functions.

**Step 4: Address selection in checkout**

Update `PlaceOrderScreen.kt` step 2 to load real addresses from `ProfileViewModel` or pass the address list via the `OrderViewModel`. Show real saved addresses as selectable rows. The "+ Add new address" row navigates to `AddressesScreen`.

**Step 5: Final check**

After all screens are wired:

1. Register a new account. Confirm you land on the home screen with real categories.
2. Log out. Confirm you land on the login screen.
3. Log in again. Confirm your account persists (it will not survive an uninstall, which is expected).
4. Browse to a product, place an order. Confirm the order appears in "My orders" with status "Processing".
5. Open the order detail. Confirm "Cancel order" is visible. Tap it. Confirm status changes to "Cancelled".
6. Check the notifications screen. Confirm the order confirmation notification is there.

Fix any crashes or navigation issues found during this check.

**Done when:** All six steps above pass without crashes. The app is fully functional from register through order tracking.
