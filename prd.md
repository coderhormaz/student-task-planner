# Student Task Planner – Premium Clean UI/UX Design Specification  
# Product Requirements Document (PRD)  
**Student Task Planner** – Android App (Java)
**Version 1.0** | February 2026  
**Goal**: Create a luxurious yet distraction-free experience — calm, elegant, fast, and addictive for students.  
Think: Notion × TickTick × Structured × Minimalist apps, but with premium polish and student-focused warmth.

## 1. Overall Design Philosophy
- **Core feeling**     : Premium calm luxury — like using a high-end digital notebook  
- **Style**            : Modern Minimalism + Material You 3 (dynamic color theming)  
- **Whitespace**       : Very generous — breathing room is luxury  
- **Typography**       : Primary: Google Sans / Inter (clean, modern)  
  Secondary: Roboto for body text (fallback)  
  Sizes: Title 28–32sp / Heading 20–24sp / Body 16sp / Caption 14sp / Small 12sp  
- **Color Palette** (Material You dynamic + manual overrides)  
  - Primary   : Soft Indigo / Teal / Warm Emerald (extracted from wallpaper)  
  - Accent     : Vibrant but desaturated (coral, mint, lavender)  
  - Surface    : #FFFFFF / #121212 (light/dark)  
  - Surface-1  : Elevated cards +1% lightness  
  - Outline    : Very subtle #00000012 / #FFFFFF20  
  - scrim      : 20–40% opacity blur for modals  
- **Corners**          : 28dp – 36dp large rounded corners (premium feel)  
- **Elevation**        : Subtle — 0dp flat + 1–4dp for cards/fabs  
- **Motion**           : Fluent, 400–600ms ease-in-out-quart; micro-interactions on check, swipe, drag  

## 2. Theme Modes
- Light (default premium feel)  
- Dark (true black + desaturated colors)  
- Dynamic (Material You – auto from wallpaper, Android 12+)  
- AMOLED pure black option in settings (toggle)  

## 3. Key Screens & Layouts

### 3.1 Splash / Onboarding
- Full-screen gradient (soft primary → surface)  
- Centered logo + tagline “Focus. Plan. Succeed.” (fading in)  
- 3–4 onboarding cards (swipeable, subtle parallax)  
  1. “One place for all your deadlines”  
  2. “Gentle nudges, never nagging”  
  3. “See your progress grow”  
  4. “Offline-first, always ready”  
- Minimal “Get Started” button (large pill, elevated)

### 3.2 Home / Today Screen (Main View)
- Top: Greeting “Good morning, Hormaz” (dynamic time-of-day) + avatar  
- Date header (large, bold, tappable → jump to calendar)  
- Today’s Focus card (prominent, glass-like blur if background image allowed)  
  - 3–4 most important tasks (auto or manual)  
  - Progress ring / streak counter  
- Task List (RecyclerView)  
  - Card per task: 24–32dp vertical padding  
  - Left: Checkbox (animated fill + scale + confetti on complete)  
  - Center: Title (bold) + subtitle (time / subject tag)  
  - Right: Priority dot (small glowing orb: low gray, med blue, high red-orange)  
  - Swipe right → complete, left → delete (with undo snackbar)  
- Floating Action Button (FAB): Large + centered at bottom, morphs on scroll  
  - Default: + icon  
  - Long press → quick add voice / photo  

### 3.3 Task Detail Screen (Modal / Full Screen)
- Large hero title (editable)  
- Description field (rich text support – bold, bullet, link preview)  
- Due date & time (beautiful date picker with calendar preview)  
- Subject / Category pill (color-coded, tappable)  
- Priority selector (3 elegant segmented buttons)  
- Sub-tasks (indented checklist, drag to reorder)  
- Attachments row (thumbnails – photo, pdf, link)  
- Bottom bar: Save | Reminder | Share | Delete  

### 3.4 Calendar View
- Three tabs: Day / Week / Month (smooth segmented control)  
- Day view: Vertical timeline (hours on left, 15-min granularity)  
  - Tasks as elegant colored blocks (rounded, subtle shadow)  
  - Drag to reschedule  
- Week view: 7-column grid, compact but readable  
- Month view: Heatmap-style dots (more intense = more tasks)  
- FAB stays → quick add to selected date  

### 3.5 Stats / Progress Dashboard
- Clean card grid (2–3 per row on phone)  
  - Completion rate donut (animated)  
  - Weekly streak calendar (like GitHub contrib graph – small squares)  
  - Subject breakdown horizontal bar chart  
  - “Focus score” big number + motivational phrase  
- Minimal line graph of tasks completed over time  

### 3.6 Settings
- Clean list with large icons + toggles  
  - Appearance: Theme picker (3 swatches + dynamic toggle)  
  - Notifications: Granular switches  
  - Backup: Google Drive / local export  
  - About: Credits, version, “Made with focus in India”  

## 4. Micro-Interactions & Animations (Premium Feel)
- Checkbox: Scale up → fill → confetti burst (small & tasteful)  
- Swipe complete: Gentle slide + fade + particle sparkle  
- FAB morph: Circle → pill when menu opens  
- Pull-to-refresh: Elegant spinner with primary color pulse  
- Task drag & drop: Lift shadow + reorder ghost  
- Empty state illustrations: Soft line art student studying / calendar / trophy  

## 5. Typography & Icon Rules
- Icons: Material Symbols Rounded (preferred) or Fluent System Icons  
- Weight: Medium for titles, Regular for body  
- Line height: 1.4–1.6× font size  
- Kerning: Slightly increased for headings (premium touch)  

## 6. Accessibility & Polish
- Minimum touch target: 48dp  
- High contrast mode support  
- Dynamic type scaling (up to +200%)  
- TalkBack friendly labels  
- Haptic feedback on complete / important actions  
- Reduce motion option disables confetti & heavy animations  

## 7. Inspiration References (2026 style)
- TickTick elegant mode  
- Structured (timeline focus)  
- MinimaList / Clear (gesture purity)  
- Notion mobile (clean cards + whitespace)  
- GoodNotes student planner layouts (warm & inviting)  
- Dribbble shots: “minimalist todo”, “student planner app clean”, “premium task manager”  
