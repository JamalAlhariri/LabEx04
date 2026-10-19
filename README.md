# COMP3074 — Room Customer Database Lab
## Complete start-to-finish Android Studio guide

**This project implements the uploaded `COMP3074_Room_Kotlin_Jetpack_Compose_Lab.docx`.** It follows the lab's Room Entity → DAO → RoomDatabase → ViewModel → Jetpack Compose structure. It also implements **Section 20 (Search)** and **Section 21 (Optional Update)**. The extra Android instrumentation tests are a convenience, not a requirement stated by the handout.

**Important difference:** This is the COMP3074 Android lab. It is **not** the earlier COMP3123 Node/Express/Vercel Assignment 1. Do not mix the submission instructions for those two classes.

### What you will finish
- A Compose-only customer UI (no XML layout).
- A persistent Room/SQLite database with customer ID, name, age and active/inactive status.
- Add, display, delete, **search** and **edit/update** customer operations.
- Automatic list refresh through Room `Flow`, without a View All button.
- Input validation, a `ViewModel`, a factory, coroutines and `LazyColumn`.
- Four optional DAO tests, test checklist and answers to ten reflection questions.

### Before you start: open this ZIP properly

This ZIP is a **source/project overlay**. It is intentionally missing a `gradle-wrapper.jar`, `gradlew`, and `gradlew.bat` because those are generated automatically by Android Studio when creating a new project. **Do not just double-click the ZIP and press Run.** Create the template first, then copy this project's contents over it.

**Requirements**: Windows PC with Android Studio; internet access for initial Gradle sync; Android SDK Platform 35; Android Studio's JDK 17; emulator or Android device (API 24+). The configuration uses AGP 8.7.3, Gradle 8.9, Kotlin/Compose plugin 2.0.21, KSP 2.0.21-1.0.28, Room 2.6.1, Compose BOM 2025.01.01 and lifecycle 2.8.7. These intentionally pinned versions provide a reproducible course-style setup. They are not advertised as the latest in 2026.

### Step 1 — Create a new Compose project in Android Studio

1. Open **Android Studio**.
2. Select **New Project** (or **File > New > New Project** if another project is open).
3. Under **Phone and Tablet**, select **Empty Activity** — choose the template whose description says **Jetpack Compose**. Do **not** select **Empty Views Activity**.
4. Click **Next**.
5. Enter exactly:
   - **Name:** `RoomCustomerLab`
   - **Package name:** `ca.gbc.comp3074.roomcustomerlab`
   - **Save location:** e.g. `C:\Users\<your-Windows-username>\AndroidStudioProjects\RoomCustomerLab`
   - **Language:** Kotlin
   - **Minimum SDK:** API 24 or higher
   - **Build configuration language:** Kotlin DSL if asked.
6. Click **Finish**. Wait for its initial Gradle sync.
7. You should see a folder named `RoomCustomerLab` containing `app`, `gradle`, `gradlew`, `gradlew.bat` and one or more `.gradle.kts` files.

**Do not start a Java project, do not create an XML layout, and do not use the old `PayCalculator` project.**

### Step 2 — Unzip and copy these prepared files

1. Download `RoomCustomerLab_Source.zip` and right-click > **Extract All**.
2. Open the extracted `RoomCustomerLab` folder.
3. Select everything **inside** that folder (including `app`, `gradle`, `settings.gradle.kts`, `build.gradle.kts`, etc.).
4. Copy these contents to the **root of the `RoomCustomerLab` folder created by Android Studio**, not to its `app` subfolder.
5. When Windows asks, select **Replace the files in the destination**.
6. Do **not delete** `gradlew`, `gradlew.bat` or `gradle/wrapper/gradle-wrapper.jar` from the Android Studio project: the prepared ZIP does not contain these files.
7. Return to Android Studio. Its Project view should now contain the packages/files described in Step 3.

If Android Studio asks whether to reload the project, approve it. The project's existing `gradle/libs.versions.toml` may remain; the prepared `.gradle.kts` files use literal versions, so the unused TOML file does not matter.

### Step 3 — Verify the source tree

In the **Project** pane, choose the `Project` dropdown view (not `Android`) if you want to check folder names precisely:

```text
RoomCustomerLab/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/ca/gbc/comp3074/roomcustomerlab/
│       │   │   ├── MainActivity.kt
│       │   │   ├── data/
│       │   │   │   ├── Customer.kt
│       │   │   │   ├── CustomerDao.kt
│       │   │   │   └── AppDatabase.kt
│       │   │   └── ui/
│       │   │       ├── CustomerScreen.kt
│       │   │       └── CustomerViewModel.kt
│       │   └── res/values/strings.xml
│       └── androidTest/java/ca/gbc/comp3074/roomcustomerlab/
│           └── CustomerDaoTest.kt
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradle/wrapper/gradle-wrapper.properties
└── README.md
```

You should also have `gradlew`, `gradlew.bat`, and `gradle/wrapper/gradle-wrapper.jar` from Android Studio's new-project template (not included in ZIP).

### Step 4 — Set the JDK and Android SDK

1. In Android Studio, open **File > Settings > Build, Execution, Deployment > Build Tools > Gradle**.
2. Set **Gradle JDK** to the bundled **JDK 17** (if the dropdown includes `jbr-17`, select that). If Android Studio is using a newer JDK and Gradle succeeds, you may leave it; JDK 17 is the conservative baseline for this configuration.
3. Go to **Tools > SDK Manager > SDK Platforms**.
4. Check **Android API 35** and install it if not already installed; accept any license prompts.
5. Make sure **Gradle Offline Mode** is off before the first sync because Room, Compose, KSP and other dependencies need downloading.

### Step 5 — Sync Gradle

1. Click **File > Sync Project with Gradle Files**, or the elephant icon in the toolbar.
2. Wait until you see `BUILD SUCCESSFUL` or `Gradle sync finished`.
3. The first sync may take several minutes as it downloads Gradle 8.9 and project libraries.
4. Ignore non-error notices suggesting a newer plugin version for now. If it prompts to upgrade all Gradle/Kotlin versions at once, select **Not now** so the pinned versions remain aligned.
5. If you see red imports before the sync finishes, wait. After sync, re-open `CustomerDao.kt` and verify that `androidx.room` imports resolve.

### Step 6 — Create or select an emulator

1. Click **Tools > Device Manager**.
2. Under **Virtual**, click **Create Device** / `+`.
3. Select a common phone such as a **Pixel 6** and click Next.
4. Choose a system image at or above API 24 (for example API 35), download it if necessary, then Finish.
5. Click the triangular **Play** icon to start the emulator.
6. You may instead connect an Android phone with **USB debugging** enabled.

### Step 7 — Run the app

1. In the main Android Studio toolbar, select the **app** run configuration.
2. Select the emulator/device you just created.
3. Click the green **Run** triangle or use **Shift+F10**.
4. Wait for the installation to finish. The application should open with the heading **Room Customer Demo**, search controls, customer name/age inputs, active switch, Add Customer button and list area.
5. On the first launch, the list should be empty.

### Step 8 — Test customer insertion

1. Tap **Customer name**, enter `Alex`.
2. Tap **Age**, enter `22`.
3. Enable the **Active customer** switch.
4. Tap **Add Customer**.
5. Expect `Customer submitted.` and a new card in the list containing **Alex**, **ID: 1** (assuming a fresh database), **Age: 22**, **Active customer**.
6. Add `Maria`, `25`, switch off; add `Jordan`, `21`, switch on.
7. The IDs should increase automatically. If you added/deleted records before, IDs may not start at 1 — that is normal.

### Step 9 — Test input validation

1. Clear Customer name and Age, then press **Add Customer**.
2. Expect **Enter a valid name and age.** Nothing should be saved.
3. Enter a name, leave Age blank and try again. Same result.
4. The Age field only accepts numeric characters; invalid text is rejected.
5. The original handout accepts integer `0` as a valid age. This project matches that basic behavior rather than adding new age restrictions.

### Step 10 — Test automatic refresh

1. Add another customer.
2. Observe that a new card appears without pressing a refresh or View All button.
3. Tap **Delete** next to any customer.
4. The card should disappear automatically.
5. This is the `Flow<List<Customer>>` + `collectAsStateWithLifecycle()` behavior explained in Sections 7, 12 and 17 of the lab.

### Step 11 — Test database persistence

1. After adding two or three customers, close the application normally.
2. Reopen **Room Customer Lab** from the Android home screen or press Run again from Android Studio.
3. The same customers should still appear because Room stores them in `customer_database` (SQLite on the device).
4. **Important**: **Uninstalling the app** or clearing app data deletes its database; ordinary closing/reopening does not.

### Step 12 — Complete Section 20: Search challenge

1. With `Alex`, `Maria` and `Jordan` saved, enter `mar` in **Search by name**.
2. Tap **Search**.
3. The list should show `Maria` only; the heading changes to **Search results (1)**.
4. Enter `xyz`, tap Search; the list should be empty.
5. Tap **Clear**. The search text clears and all stored customers return.
6. Search works through the DAO SQL query and the ViewModel's `searchResults` Flow. The same `CustomerList` composable displays all customers or matching results.

### Step 13 — Complete Section 21: Optional update challenge

1. Tap **Edit** beside `Alex`.
2. Notice the form is prefilled with the selected name, age and active status; the button reads **Save Changes**.
3. Change the name to `Alex Smith`, age to `23`, or toggle the switch.
4. Tap **Save Changes**.
5. The card updates and keeps the **same customer ID**.
6. To abort an edit, tap **Cancel Editing**.
7. Update is optional in the handout, but included in this prepared project.

### Step 14 — (Optional) Run automated DAO tests

1. In the Android Studio Project pane, find `app/src/androidTest/.../CustomerDaoTest.kt`.
2. Right-click the `CustomerDaoTest` class/file and choose **Run 'CustomerDaoTest'**.
3. Select a running Android emulator/device if prompted.
4. The four tests cover **insert**, **search**, **update**, and **delete** using an in-memory Room database.
5. All four should pass; if they fail with environment/device errors, first ensure the emulator booted fully.

### Step 15 — (Optional) Inspect the actual Room database

1. Run the app and create some customers.
2. Choose **View > Tool Windows > App Inspection**, then **Database Inspector**.
3. Select the running app process, `ca.gbc.comp3074.roomcustomerlab`.
4. Open `customer_database` and the `customers` table.
5. Confirm columns `id`, `name`, `age` and `isActive` (SQLite may store Boolean internally as an integer). Room maps the Boolean automatically.

### Step 16 — Capture evidence screenshots if your instructor asks for them

The attached lab handout **does not specify a screenshot requirement, exact screenshot count, a GitHub URL requirement, or a D2L submission method**. Check the actual course D2L activity or instructor instructions before submitting anything. If evidence is requested, these screenshots are useful:

1. **App first run** showing an empty list.
2. **Customer inserted** showing 2–3 customer cards with IDs and active/inactive status.
3. **Search results** showing a filtered customer list.
4. **Edit in progress** showing prepopulated form and Save Changes.
5. **Updated record** showing changed customer with the same ID.
6. **Validation message** when name or age is missing.
7. **Project file tree** in Android Studio, if requested.
8. **Database Inspector** showing the Room table, if requested.

On Windows: use **Windows + Shift + S** and save the capture. In Android Studio's emulator, use the emulator toolbar's **Take Screenshot** (camera icon or More menu). If required, paste screenshots into a Word document with simple titles, for example `COMP3074 Room Database Lab - Screenshots`.

### Step 17 — Save, zip and/or publish only as directed by D2L

1. In Android Studio, select **File > Save All** (or Ctrl+S).
2. To create a project ZIP for sharing, **close** Android Studio or wait until it is no longer building.
3. In Windows File Explorer, open the **RoomCustomerLab** project folder.
4. When packaging, include `app/src`, all `.gradle.kts` files, `gradle/wrapper/gradle-wrapper.properties`, `gradle/wrapper/gradle-wrapper.jar`, `gradlew`, `gradlew.bat`, and `gradle.properties`.
5. Exclude generated or local data like `.gradle`, `.idea`, `app/build`, `build`, `local.properties`, and captures; do not accidentally delete them from your original work folder.
6. If D2L specifically requires source ZIP, make a copy of the project and remove those generated folders from the **copy**, then zip the copy.
7. If a GitHub repository is requested, create a new empty repository, open the project terminal, and use:

```powershell
git init
git add .
git commit -m "Complete Room customer database lab"
git branch -M main
git remote add origin https://github.com/<YOUR-USERNAME>/RoomCustomerLab.git
git push -u origin main
```

Do not run `git remote add origin` a second time if one already exists. Replace `<YOUR-USERNAME>` with your own GitHub user name and use the URL GitHub shows you. **Do not submit this project as your earlier Node/Vercel assignment.**

### Step 18 — Explanation of each source file

| File | Why it exists | Handout section |
|---|---|---|
| `Customer.kt` | `@Entity`; auto-generated primary key; name, age, isActive | 5 |
| `CustomerDao.kt` | `@Insert`, `@Delete`, `@Update`, SELECT and search SQL | 6, 20, 21 |
| `AppDatabase.kt` | Single Room database instance, database version 1 | 8 |
| `CustomerViewModel.kt` | `viewModelScope` coroutines, data Flow, search and update | 9, 10, 20, 21 |
| `MainActivity.kt` | Opens database and sets up Material 3 Compose UI | 11 |
| `CustomerScreen.kt` | Form, validation, search buttons, LazyColumn, rows, edit form | 12–15, 20, 21 |
| `app/build.gradle.kts` | Room/KSP/Compose/Lifecycle dependencies | 4 |
| `AndroidManifest.xml` | Android launcher activity and app label | Project setup |
| `CustomerDaoTest.kt` | Optional four in-memory DAO tests | Extra verification |

### Step 19 — The architecture in plain English

```text
User taps Add / Search / Save Changes / Delete
                    ↓
CustomerScreen and CustomerApp (Compose UI)
                    ↓
CustomerViewModel (events + coroutines + Flow)
                    ↓
CustomerDao (@Insert, @Delete, @Update, @Query)
                    ↓
AppDatabase (RoomDatabase)
                    ↓
SQLite customer_database file on the device
                    ↓
DAO Flow emits changed list → Compose recomposes screen
```

There is no `SQLiteOpenHelper`, `ContentValues`, `Cursor`, `rawQuery` or manual list-refresh operation in application source: the main point of the lab is that Room handles the database-mapping boilerplate while still storing data in SQLite.

### Step 20 — Answers to all ten reflection questions (Section 22)

**1. Is Room a replacement for SQLite, or an abstraction over SQLite?** Room is an abstraction on top of SQLite. SQLite still stores the actual data.

**2. What is the role of `@Entity`?** It tells Room that the Kotlin data class represents a database table. Every object is one row.

**3. What is the role of a DAO?** A DAO defines the allowed database operations, such as inserting, deleting, updating and getting customers.

**4. Why are insert and delete declared `suspend` functions?** Database work may take time. Suspend functions let coroutines perform it without blocking the main UI thread.

**5. Why can a SELECT query return `Flow<List<Customer>>`?** Room can watch a table and send a new list whenever the database data changes.

**6. Why is `Cursor` no longer visible?** Room reads the SQL rows and automatically converts them into `Customer` objects.

**7. Why does the list update without a View All button?** The UI collects the Room Flow; adding, deleting and updating customers automatically triggers an updated list.

**8. What responsibility does `CustomerViewModel` have?** It connects UI actions to DAO operations, launches database coroutines, handles the search query and exposes data for the screen.

**9. What advantages does Room provide over `SQLiteOpenHelper`?** Room uses less repetitive code, checks SQL queries during compilation, supports Kotlin data classes, coroutines, Flow and makes the UI/database separation easier.

**10. What did direct SQLite teach that is hidden by Room?** It showed how tables, SQL queries, ContentValues, Cursors and manual row conversion work under the abstraction.

### Step 21 — Common errors and exactly what to do

**A. `Plugin [id: 'com.google.devtools.ksp'] was not found`**  
Open root `build.gradle.kts`, check that KSP plugin version is present. Open `settings.gradle.kts` and verify `google()`, `mavenCentral()` and `gradlePluginPortal()` are in `pluginManagement`. Turn Gradle Offline Mode off, check internet, then **Sync Project with Gradle Files**.

**B. `Unresolved reference: room`, `@Entity`, `RoomDatabase`, or `ksp`**  
Finish Gradle sync; confirm app dependencies `room-runtime`, `room-ktx`, `room-compiler` and `ksp(...)` in `app/build.gradle.kts`. If no sync finishes, expand the Build window and fix its **first** error.

**C. `Android Gradle plugin requires Java 17` / Java mismatch**  
Set Android Studio **File > Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JDK** to JDK 17; resync.

**D. `Failed to find Platform SDK with path platforms;android-35`**  
Install **Android API 35** using Tools > SDK Manager > SDK Platforms, then resync.

**E. `Could not find androidx.compose...` or Maven artifact**  
Verify internet; disable Gradle Offline Mode; the project uses Google and Maven Central repositories in `settings.gradle.kts`. Try sync again. Leave pinned versions unchanged while troubleshooting.

**F. `Class ... MainActivity not found` or app crashes immediately**  
Confirm all Kotlin package lines use `ca.gbc.comp3074.roomcustomerlab`; check the `namespace` and `applicationId` match in app Gradle; make sure the manifest activity is `.MainActivity`. If you created the template with a different package, **create it again with the exact specified package** instead of doing many package renames.

**G. Duplicate function/classes errors**  
Check that you did not paste new code below an old `MainActivity` class or keep two copies of `CustomerScreen`. Each file should contain only its prepared contents. Keep the `ui/theme` folder from the template if you want; it is not used and not harmful.

**H. `Cannot access database on the main thread`**  
Do not call Room writes directly from a Composable. The provided ViewModel uses `viewModelScope.launch` with DAO suspend functions. Restore that pattern.

**I. Database changed / schema migration error after editing entity**  
During development, uninstall the app from the emulator and run again. This clears the test database. For a real production app, increase the schema version and provide a migration; do not rely on uninstalling.

**J. App installs but list never changes**  
Make sure `CustomerDao.getAllCustomers()` returns `Flow<List<Customer>>`; the ViewModel exposes Flow and `CustomerApp` uses `collectAsStateWithLifecycle`; the list receives those results. Do not replace it with a local one-time list query.

**K. Emulator won't start**  
Use Device Manager to cold boot/wipe a stuck emulator, or create a new emulator. Check Windows virtualization support and free disk space. You may connect a real Android device instead.

**L. Unable to edit because the list is tiny or keyboard covers it**  
Dismiss the keyboard with Android's back button, then scroll the list. On a small emulator choose a taller phone (for example Pixel 6), rotate portrait, or reduce Windows display scaling.

**M. Tests show failures**  
Verify the device finished booting; right-click `CustomerDaoTest.kt` and run as **Android Instrumented Tests**, not ordinary JUnit desktop tests. The file is under `src/androidTest` for a reason.

### Final verification checklist

- [ ] Gradle sync succeeds.
- [ ] Android app launches (Compose screen, no XML layout).
- [ ] Empty list message appears on a new installation.
- [ ] Add 3 customers; IDs auto-increment.
- [ ] Active switch state shows correctly in the list.
- [ ] Empty name/age prevented.
- [ ] Close/reopen app retains records.
- [ ] Delete removes card immediately.
- [ ] Search by name fragment works; Clear shows all.
- [ ] Edit/Save Changes modifies customer without changing ID.
- [ ] Optional DAO tests pass (if run).
- [ ] Save screenshot(s) and zip/GitHub only if your COMP3074 D2L instructions request them.

### What has and has not been verified

The prepared files were checked for their internal structure and required lab features. They **have not been compiled or run on an Android emulator in this environment**, because an Android SDK, Gradle build tooling and the required downloaded libraries are not installed here. The actual Gradle sync and app run happen on your Windows PC in Android Studio.
