# Fall Detection System

## Dataset
The dataset used for this project is available on Kaggle: [Human Activity Recognition with Mobile Sensing](https://www.kaggle.com/datasets/uciml/human-activity-recognition-with-smartphones).

## Overview
The Fall Detection System is a mobile health technology designed for elderly care, focusing on detecting falls and recognizing activities. This application leverages smartphone sensors to provide real-time alerts to caregivers or emergency contacts, thereby improving safety and reducing response times for fall incidents.

## Features
- **Fall Detection**: Utilizes built-in smartphone accelerometer and gyroscope sensors to detect falls.
- **Activity Recognition**: Monitors various activities to differentiate between normal and abnormal behavior.
- **Real-time Alerts**: Sends immediate notifications to caregivers or emergency contacts in case of a fall.

## Technologies Used
- **Programming Language**: Kotlin
- **Development Environment**: Android Studio
- **Machine Learning Framework**: TensorFlow
- **Algorithms**: 
  - KNN: 0.89
  - Random Forest: 0.92
  - MLP: 0.94
  - CNN: 0.92
  - LSTM: 0.946
# Fall Detection and Activity Prediction App

Welcome to the **Fall Detection and Activity Prediction App** repository! This project was developed for the Mobile Application Development course at Amrita School of Computing. Our app combines **AI-powered Fall Detection** and **Activity Prediction** features, designed to enhance safety for elderly individuals or anyone needing extra support in daily movements.

## 📋 Project Overview
This app utilizes machine learning and real-time smartphone sensors (accelerometer) to:
- Detect potential falls and notify caregivers if necessary.
- Predict daily activities such as sitting, standing, walking, and more, to provide insight into users' mobility and activity levels.

## 🚀 Features
1. **User Authentication**
   - Secure Sign-Up and Login.
   - Credentials are stored locally for a smooth, secure experience each time.

2. **Real-Time Sensor Data & Visualization**
   - Displays accelerometer readings along the x, y, and z axes in a real-time graph.
   - Captures and visualizes motion data for activity recognition and fall detection.

3. **Fall Detection with Emergency Alerts**
   - Detects potential falls based on sensor data and prompts a recovery check.
   - If the user does not respond, the app automatically sends an alert to a designated contact, informing them of the potential fall incident.

4. **Activity Prediction System**
   - Identifies six activities: sitting, standing, walking, walking upstairs, walking downstairs, and lying down.
   - Uses machine learning models trained on accelerometer data from Kaggle datasets and implemented with TensorFlow Lite for on-device prediction.

## 🛠️ Technologies Used
- **Android Studio** - Integrated development environment for Android.
- **Kotlin** - Primary programming language for app development.
- **TensorFlow Lite** - Machine learning framework for on-device AI.
- **Accelerometer Sensor** - Built-in mobile sensor for activity detection and fall recognition.

## 📲 Getting Started
### Prerequisites
- Android Studio installed on your development environment.
- A physical Android device (preferred) or emulator that supports accelerometer sensors.

### Installation
1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/FallDetectionActivityPredictionApp.git


## Contributors
- CB.EN.U4CSE21605
- CB.EN.U4CSE21162


