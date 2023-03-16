# GPS Tracer

This is a practice project of Module 335 "Mobile-Applikation realisieren".

## Setup

This project uses the Mapbox Maps SDK for Android. Therefore, in order to run the app,
you'll need an API key. Create a Mapbox Account and configure your **secret** token as described
in the official documentation: https://docs.mapbox.com/android/maps/guides/install/

In order to set the public key, you'll need to create `developer-config.xml` in `app/res/values/`.
Add the following content to it and replace `MAPBOX_PUBLIC_KEY` with your personal API key.

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="mapbox_access_token">MAPBOX_PUBLIC_KEY</string>
</resources>
```