# 📕 Wikimon – Enciclopedia Interactiva Móvil

![Status](https://img.shields.io/badge/Status-Completado-green)
![Category](https://img.shields.io/badge/Category-Entretenimiento%20%2F%20Consulta-purple)
![Stack](https://img.shields.io/badge/Stack-Kotlin%20%7C%20Retrofit2%20%7C%20Coroutines-blue)

**Wikimon** es una aplicación móvil nativa desarrollada para Android, diseñada para la consulta interactiva de criaturas e información enciclopédica. El proyecto se centra en ofrecer un rendimiento de lectura fluido, integración con APIs externas y una experiencia de usuario reactiva mediante la gestión asíncrona de datos.

---

## 🎯 Propósito del Proyecto

El objetivo principal de Wikimon es demostrar el dominio en el consumo de servicios web externos y la optimización de recursos en dispositivos móviles:

- **Consumo eficiente de APIs:** conexión asíncrona con endpoints REST sin bloquear la interfaz de usuario.
- **Filtrado en tiempo real:** algoritmos de búsqueda instantánea y filtrado dinámico sobre grandes colecciones de datos.
- **Gestión de estados de UI:** control del ciclo de vida y manejo de estados de carga, éxito y error para conseguir una navegación fluida.

---

## 📐 Arquitectura del Sistema

El proyecto sigue una arquitectura por capas basada en el patrón **Repository**, desacoplando la capa de red de la interfaz mediante **ViewModel** y **Kotlin Coroutines**.

```mermaid
graph TD
    A[UI Layer - Activities / Views] --> B[ViewModel / StateFlow]
    B --> C[Repository Layer]
    C --> D[Network Service - Retrofit2 / OkHttp]
    D -- "JSON Response" --> E[External REST API]

    style A fill:#3DDC84,stroke:#333,color:#fff
    style B fill:#0073E6,stroke:#333,color:#fff
    style C fill:#6DB33F,stroke:#333,color:#fff
    style D fill:#336791,stroke:#333,color:#fff
    style E fill:#FF9900,stroke:#333,color:#000
```

---

## 💻 Implementación Técnica

### 🛠️ Capa de Red y Datos (Network Layer)

- **Retrofit 2 y OkHttp:** configuración del cliente HTTP para la serialización y el procesamiento de respuestas JSON.
- **Kotlin Coroutines:** ejecución de operaciones asíncronas mediante `Dispatchers.IO` para evitar bloquear el hilo principal (`Main Thread`).
- **Caché y carga de imágenes:** integración de librerías como Coil o Glide para optimizar el uso de memoria RAM y datos móviles.

### 🎨 Interfaz de Usuario (UI Layer)

- **Componentes reactivos:** uso de `RecyclerView` con adaptadores optimizados mediante `DiffUtil` para renderizar listas de elementos de forma eficiente.
- **Búsqueda dinámica:** filtrado en tiempo real por nombre, tipo o atributos.
- **Material Design:** componentes visuales orientados a mantener una interfaz consistente y una experiencia de usuario intuitiva.

---

## 🛠️ Stack Tecnológico

| Categoría | Herramientas / Tecnologías |
|---|---|
| Lenguaje | Kotlin |
| Networking | Retrofit 2, OkHttp, Gson |
| Asincronía | Kotlin Coroutines |
| UI Framework | Android SDK, Material Design, ViewBinding |
| Carga de imágenes | Coil / Glide |
| Arquitectura | MVVM / Repository Pattern |
| Herramientas | Android Studio, Git / GitHub |

---

## 👤 Contacto
<div align="left">
  <a href="https://www.linkedin.com/in/camu-al/" target="_blank">
    <img src="https://img.shields.io/static/v1?message=LinkedIn&logo=linkedin&label=&color=0077B5&logoColor=white&style=flat" height="40" />
  </a>

  <a href="mailto:camudeveloper@gmail.com" target="_blank">
    <img src="https://img.shields.io/static/v1?message=Gmail&logo=gmail&label=&color=D14836&logoColor=white&style=flat" height="40" />
  </a>

  <a href="mailto:camudeveloper@gmail.com" target="_blank">
    <img src="https://img.shields.io/static/v1?message=Gmail&logo=gmail&label=&color=D14836&logoColor=white&style=flat" height="40" />
  </a>
