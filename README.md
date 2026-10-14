# 🌟 AR Studio — Augmented Reality Application

[![Java](https://img.shields.io/badge/Language-Java_8%2F17-orange.svg)](https://www.oracle.com/java/)
[![Android](https://img.shields.io/badge/Platform-Android_7.0%2B-green.svg)](https://developer.android.com/)
[![ARCore](https://img.shields.io/badge/AR_Engine-Google_ARCore_1.45-blue.svg)](https://developers.google.com/ar)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](LICENSE)

An Augmented Reality application built using pure **Java & XML**, **Google ARCore**, and **Sceneform**. **AR Studio** detects real-world horizontal surfaces (floors, desks, tables) using Visual-Inertial Odometry (VIO) and allows users to anchor 3D objects with intuitive touch gestures.

---

## 📋 Academic Marking Rubric Compliance

This project has been engineered specifically to satisfy all criteria of the **Java Project Evaluation Rubric**:

| Evaluation Criteria | Marks | Implementation Details in Code |
| :--- | :---: | :--- |
| **OOP Implementation** | **10** | • **Interfaces:** `ARGeometry`, `GenericDao<T, ID>`, `SceneDao`<br>• **Inheritance & Abstraction:** `AbstractGeometry` extended by `PrimitiveGeometry` and `CompoundGeometry`<br>• **Polymorphism:** Dynamic volume and geometry calculation in `PrimitiveGeometry` vs `CompoundGeometry`<br>• **Exception Handling:** Custom hierarchy: `ARCustomException`, `DatabaseOperationException`, `SurfaceNotDetectedException` |
| **Collections & Generics** | **6** | • **Generic DAO:** `GenericDao<T, ID>` with generic CRUD contracts<br>• **Collections:** `List<PlacedObjectRecord>`, `Map<ArObjectType, ARModelEntity>`, `ArrayList<PrimitiveGeometry>` |
| **Multithreading & Synchronization** | **4** | • `ARThreadPoolManager`: Fixed thread pool with `ExecutorService`<br>• `synchronized` critical sections for database writes & scene updates<br>• `AtomicInteger` for active task tracking and safe UI thread dispatching via `Handler` |
| **Database Operations (DAO)** | **7** | • `SceneDao` & `SceneDaoImpl`: Complete CRUD (`insert`, `findById`, `findAll`, `deleteById`, `count`)<br>• Transaction management with `beginTransaction()` and `setTransactionSuccessful()` |
| **Database Connectivity (SQLite / SQL)** | **6** | • `AppDatabaseHelper`: Thread-safe Singleton `SQLiteOpenHelper`<br>• Relational schemas: `scenes` and `placed_objects` with Foreign Keys |

---

## 📱 Features

- **Real-Time AR Viewfinder:** 60 FPS camera feed with dot-grid surface detection.
- **6 Distinct 3D Objects:**
  - `Cube` (Procedural PBR Box)
  - `Sphere` (Specular Ruby Sphere)
  - `Cylinder` (Emerald Cylinder)
  - `Chair` (Compound Architectural Chair)
  - `Table` (Modern Study Table)
  - `Plant` (Ceramic Planter with Foliage)
- **Multi-Touch Gestures:**
  - One-finger drag to translate across detected plane.
  - Two-finger pinch to scale smoothly (0.1x to 5.0x).
  - Two-finger twist to rotate around Y-axis.
- **Camera Crash Prevention:** Full runtime camera permission verification, pre-session ARCore availability validation, and crash-resistant Android Activity lifecycle hooks.
- **Database Persistence:** Save placed AR scenes with timestamps and reload saved scenes.

---

## 🏗️ Project Architecture

```text
com.arstudio
├── ArActivity.java                 # Main AR viewfinder & gesture controller
├── MainActivity.java               # Home dashboard with modern clean UI
├── GalleryActivity.java            # 3D model gallery with physical dimensions
├── HowArWorksActivity.java         # Educational screen explaining SLAM & VIO
├── AboutActivity.java              # Technical architecture details
├── ar/
│   ├── CustomArFragment.java       # Configured AR scene fragment
│   └── ObjectRenderableFactory.java# Procedural 3D model builder
├── concurrency/
│   └── ARThreadPoolManager.java    # ExecutorService & thread synchronization
├── db/
│   ├── AppDatabaseHelper.java      # SQLite connection & schema manager
│   ├── GenericDao.java             # Generic CRUD interface (Generics & Collections)
│   ├── SceneDao.java               # Scene DAO interface
│   ├── SceneDaoImpl.java           # Concrete SQL database operations
│   ├── SceneRecord.java            # Scene database model entity
│   └── PlacedObjectRecord.java     # Placed 3D object database model entity
├── exception/
│   ├── ARCustomException.java      # Base custom exception
│   ├── DatabaseOperationException.java
│   └── SurfaceNotDetectedException.java
├── model/
│   ├── ArObjectType.java           # 3D object enumeration
│   ├── PlacedObject.java           # Placed node tracking
│   └── geometry/
│       ├── ARGeometry.java         # Geometry interface (OOP)
│       ├── AbstractGeometry.java   # Abstract base class (Inheritance)
│       ├── PrimitiveGeometry.java  # Primitive shapes (Polymorphism)
│       └── CompoundGeometry.java   # Multi-part compound models
└── util/
    └── ARCoreHelper.java           # Device compatibility & permission checks
```

---

## 🚀 How to Build & Run

### Prerequisites
- **Android Studio** Hedgehog / Iguana / Jellyfish (or command-line Gradle)
- **JDK 17**
- **Android Device** with Android 7.0 (API 24) or higher and Google Play Services for AR installed.

### Option 1: Direct APK Installation (Quickest)
An installable debug APK is pre-built and ready to install:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Option 2: Build from Source
1. Clone this repository:
   ```bash
   git clone https://github.com/<your-username>/AR-Studio.git
   cd AR-Studio
   ```
2. Build the project using Gradle:
   ```bash
   ./gradlew assembleDebug
   ```
3. Open in **Android Studio** and click **Run** (`Shift + F10`).

---

## 🔬 How AR Works (Theory)
1. **Simultaneous Localization and Mapping (SLAM):** Maps unknown room surroundings while estimating device position.
2. **Visual-Inertial Odometry (VIO):** Combines 60 Hz optical camera frames with 100+ Hz IMU gyroscope & accelerometer data for millimeter 6DoF tracking.
3. **Plane Detection:** Groups coplanar feature points satisfying $Ax + By + Cz + D = 0$.
4. **Hit-Testing / Raycasting:** Projects a 3D ray from the 2D touch point to compute real-world coordinate intersections.

---

## 📄 License
This project is licensed under the MIT License.
