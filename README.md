# Products-MVVM-using-stateFlow
Refactored the Product project MVVM from LiveData to State Flow

MVVM Product App (Refactored to StateFlow)
This is a product management application built using the MVVM (Model-View-ViewModel) architectural pattern. The application manages products, allows adding/removing products from favorites, and supports fetching products from either a local or remote data source.

Refactor Overview
In this refactor, we replaced LiveData with StateFlow, a part of Kotlin's Flow API, to handle state management. StateFlow offers a more reactive and modern approach to managing UI states in comparison to LiveData. The refactor aims to improve the performance, maintainability, and scalability of the project.

Features
Fetch Products: Allows fetching products from either a remote API or local database.

Add to Favorites: Lets users add products to their favorites.

Delete from Favorites: Allows users to remove products from their favorites.

State Management: Utilizes StateFlow to manage UI states like loading, success, and error.

Error Handling: Uses a Resource sealed class to handle success, loading, and error states across the app.

Architecture
Model: The repository (ProductRepository) interacts with both the remote data source and local data source to manage the data.

ViewModel: The ProductViewModel handles business logic and exposes the state via StateFlow.

View: The UI layer observes the StateFlow and updates the UI accordingly
