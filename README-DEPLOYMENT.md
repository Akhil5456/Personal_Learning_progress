# Deployment Guide for Personal Learning Tracker

## Current Status
Your application is successfully built and the JAR file is available via GitHub Actions. However, GitHub Pages can only host static content, not dynamic Spring Boot applications.

## Deployment Options

### Option 1: Heroku (Recommended - Free Tier Available)
1. Create a Heroku account at https://signup.heroku.com/
2. Install Heroku CLI or use the web interface
3. Create a new app: `heroku create your-app-name`
4. Add Heroku secrets to your GitHub repository:
   - `HEROKU_API_KEY`: Your Heroku API key
   - `HEROKU_EMAIL`: Your Heroku email
   - `HEROKU_APP_NAME`: Your Heroku app name
5. Push to trigger deployment

### Option 2: Railway (Easy Setup)
1. Sign up at https://railway.app/
2. Connect your GitHub repository
3. Railway will auto-detect your Spring Boot app
4. Deploy with one click

### Option 3: Render (Free Tier Available)
1. Sign up at https://render.com/
2. Connect your GitHub repository
3. Create a "Web Service"
4. Point to your main branch
5. Render will deploy automatically

### Option 4: AWS/Azure/GCP (Enterprise)
More complex setup with paid tiers, but offers more control.

## Quick Start with Heroku

```bash
# Install Heroku CLI
npm install -g heroku

# Login
heroku login

# Create app
heroku create personal-learning-tracker

# Set environment variables
heroku config:set JAVA_VERSION=21

# Deploy
git push heroku main
```

## After Deployment
Your live application will be available at:
- Heroku: `https://your-app-name.herokuapp.com`
- Railway: `https://your-app-name.railway.app`
- Render: `https://your-app-name.onrender.com`

## Notes
- The H2 database will work for development
- For production, consider PostgreSQL or MySQL
- All platforms offer free tiers for small applications
