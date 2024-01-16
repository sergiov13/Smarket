# Offers Database API

## Overview
This API provides access to a marketplace's offers database. It includes functionality to retrieve and manage offers, allowing users to view all available offers and remove specific ones using either their name or barcode.

## Database Structure
The database consists of a table with the following 5 fields:

- `id`: Unique identifier for the offer.
- `title`: Name of the product.
- `price`: Product price in EUR.
- `barcode`: Product barcode.
- `quantity`: Number of units available in stock.

## API Endpoints

### Home
- **Endpoint**: `/api/home`
- **Description**: Leads to an interactive page to access the features of the API.

### View Offers
- **Endpoint**: `/api/offers`
- **Description**: Provides an HTML table containing information on all the offers present in the database.

### Remove Offer by Barcode
- **Endpoint**: `/api/remove_offer/barcode?string:barcode`
- **Description**: Removes the offer that has the given barcode in its barcode field.

### Remove Offer by Name
- **Endpoint**: `/api/remove_offer/name?string:name`
- **Description**: Removes the offer that has the given name in its title field.
