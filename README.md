<div align="center">
<img width="1200" height="475" alt="GHBanner" src="https://ai.google.dev/static/site-assets/images/share-ais-513315318.png" />
</div>

# Run and deploy your AI Studio app

This contains everything you need to run your app locally.

View your app in AI Studio: https://ai.studio/apps/6d726390-6ea3-4a4e-ac28-34ef3bb93f3f

## Run Locally

**Prerequisites:**  [Android Studio](https://developer.android.com/studio)


1. Open Android Studio
2. Select **Open** and choose the directory containing this project
3. Allow Android Studio to fix any incompatibilities as it imports the project.
4. Create a file named `.env` in the project directory and set `GEMINI_API_KEY` in that file to your Gemini API key (see `.env.example` for an example)
5. Remove this line from the app's `build.gradle.kts` file: `signingConfig = signingConfigs.getByName("debugConfig")`
6. Run the app on an emulator or physical device

## Deploy to Vercel

1. Add your Vercel project settings if you haven't already.
2. Deploy from the repo root with the Vercel CLI or from the Vercel dashboard.
3. The app uses the existing `build` script and outputs to `dist`.
4. A `vercel.json` file is included to route all requests to `index.html` for SPA support.

> If you use Vercel, make sure the project has `VERCEL_TOKEN`, `VERCEL_ORG_ID`, and `VERCEL_PROJECT_ID` configured in GitHub Actions for preview deployments.
