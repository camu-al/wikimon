# 📕 Wikimon – Enciclopedia Interactiva Móvil

![Status](https://img.shields.io/badge/Status-Completado-green)
![Category](https://img.shields.io/badge/Category-Entretenimiento%20%2F%20Consulta-purple)
![Stack](https://img.shields.io/badge/Stack-Kotlin%20%7C%20Retrofit2%20%7C%20Coroutines-blue)

**Wikimon** es una aplicación móvil nativa desarrollada para Android diseñada para la consulta interactiva de criaturas e información enciclopédica. El proyecto se centra en ofrecer un rendimiento de lectura fluido, integración con APIs externas y una experiencia de usuario reactiva mediante la gestión asíncrona de datos.

## 🎯 Propósito del Proyecto

El objetivo principal de Wikimon es demostrar el dominio en el consumo de servicios web externos y la optimización de recursos en dispositivos móviles:

- **Consumo Eficiente de APIs:** Conexión asíncrona con endpoints REST sin bloquear la interfaz de usuario.

- **Filtrado en Tiempo Real:** Algoritmos de búsqueda instantánea y filtrado dinámico sobre grandes colecciones de datos.

- **Gestión de Estados de UI:** Control riguroso del ciclo de vida y manejo de estados (cargando, éxito, error) para una navegación sin interrupciones.


## 📐 Arquitectura del Sistema

El proyecto sigue una arquitectura por capas basada en el patrón **Repository**, desacoplando la capa de red del consumo en la interfaz mediante **ViewModel** y **Kotlin Coroutines**.

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
