# ⏰ ZonaHoraria — Conversor de horas y zonas horarias

¡Bienvenido! Este proyecto convierte horas entre distintas **zonas horarias** y muestra la hora local de forma clara y agradable. Ideal para quienes trabajan con equipos internacionales o viajan con frecuencia. 🌍✈️

---

## ✨ Características

- 🔁 **Conversión entre zonas horarias** (ej. Madrid → Bogotá).
- 🗂️ **Dos implementaciones** del UI:
  - `ZonaHorariaBinding` → enfoque con *binding* (ej. ViewBinding/Binding manual).
  - `ZonaHorariaNormal` → enfoque “normal” (sin binding), útil para comparar.
- 🧪 **Validación de entrada** (horas, minutos).
- 🧭 **Detección de zona horaria local**.
- 💡 **Diseño claro** para aprendizaje y demo.

---

## 🧰 Tecnologías

- 🤖 **Android Studio / IntelliJ IDEA**
- ☕ **Java** / 🧁 **Kotlin**
- 🧱 **Gradle** (build y dependencias)
- 🧷 **ViewBinding / Binding**
- 🕰️ **java.time / Android Time APIs**

---

## 🗂️ Estructura del proyecto

```text
.
├─ ZonaHorariaBinding/          # 💚 Implementación usando Binding (p. ej. ViewBinding)
│  ├─ app/                      # Código fuente de la app (Activity, Fragments, adapters…)
│  ├─ build.gradle              # Config particular del módulo
│  └─ ...
│
├─ ZonaHorariaNormal/           # 🩶 Implementación “normal” sin Binding (para comparación)
│  ├─ app/
│  ├─ build.gradle
│  └─ ...
│
├─ docs/
│  └─ screenshots/              # 📸 Capturas usadas en este README
│     ├─ home.png
│     ├─ convert.png
│     └─ result.png
│
├─ settings.gradle              # Configuración de módulos del proyecto
├─ build.gradle                 # Gradle de nivel raíz
└─ README.md                    # Este archivo 😄
```

---

## 🚀 Cómo clonar y ejecutar

### Opción A — Android Studio 🧑‍💻
1. **Clonar** el repositorio:
   ```bash
   git clone https://github.com/Ivannovichh/ZonaHoraria.git
   ```
2. **Abrir** el proyecto en **Android Studio** (`File → Open…` y selecciona la carpeta del repo).
3. Espera a que **Gradle** sincronice y descargue dependencias.
4. Elige el **módulo** que quieres ejecutar:
   - ✅ `ZonaHorariaBinding`
   - ✅ `ZonaHorariaNormal`
5. Dale a **Run ▶** sobre un **emulador** o un **dispositivo físico**.

### Opción B — Gradle (línea de comandos) 🧪
```bash
git clone https://github.com/Ivannovichh/ZonaHoraria.git
cd ZonaHoraria
./gradlew :ZonaHorariaBinding:assembleDebug
# o
./gradlew :ZonaHorariaNormal:assembleDebug
```

---

## 🧭 Uso (paso a paso)

1. 📥 Abre la app.
2. 🕰️ Introduce una **hora** (HH:mm).
3. 🌐 Selecciona la **zona horaria de origen** y la **zona horaria de destino**.
4. 🔄 Pulsa **Convertir**.
5. ✅ Visualiza el resultado con la hora convertida.

---

## 🧪 Tests

```bash
./gradlew test
./gradlew connectedAndroidTest
```

---

## 🧹 Buenas prácticas y lint

```bash
./gradlew lint
./gradlew spotlessApply
```

---

## Autores

Miguel ángel Pérez Martín
Iván Sánchez Juárez
