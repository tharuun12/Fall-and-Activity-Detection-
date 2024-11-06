# Fall-and-Activity-Detection-App

Welcome to the **Fall and Activity Detection App** repository! This project combines cutting-edge mobile health technology with machine learning to provide real-time **Fall Detection** and **Activity Recognition** for improved safety, especially designed for elderly care. Built as part of the Mobile Application Development course at **Amrita School of Computing**, this app enhances user safety by notifying caregivers or emergency contacts in the event of a fall.

## 🏥 Fall Detection System Overview
The **Fall Detection System** aims to improve safety and response times by monitoring movements through smartphone sensors and sending immediate alerts in emergencies. This system supports independent living by providing users and their families with peace of mind.

### 📊 Dataset
The dataset used for training this application’s machine learning models is sourced from Kaggle: [Human Activity Recognition with Mobile Sensing](https://www.kaggle.com/code/malekzadeh/human-activity-recognition-with-mobile-sensing).

## 🌟 Key Features
- **User Authentication**: Secure Sign-Up and Login to protect user data.
- **Real-Time Sensor Data & Visualization**: Graphs displaying accelerometer and gyroscope data in real time, capturing x, y, and z axes.
- **Fall Detection**:
  - Uses smartphone accelerometer and gyroscope sensors to detect sudden falls.
  - Prompts user recovery checks and sends alerts if unresponsive.
- **Activity Recognition**:
  - Monitors activities to identify six states: sitting, standing, walking, walking upstairs, walking downstairs, and lying down.
  - Uses TensorFlow Lite models trained on accelerometer data.
- **Real-Time Alerts**:
  - Immediate notifications to caregivers if a fall occurs without user response.
  - Prioritizes safety with prompt alerts to emergency contacts.

## 🧑‍💻 Technologies and Tools
- **Programming Language**: Kotlin
- **Development Environment**: Android Studio
- **Machine Learning Framework**: TensorFlow Lite for on-device AI processing
- **Sensors Used**: Accelerometer and Gyroscope

### 🚀 Algorithms & Model Performance
- **K-Nearest Neighbors (KNN)**: Accuracy - 89%
- **Random Forest**: Accuracy - 92%
- **Multi-Layer Perceptron (MLP)**: Accuracy - 94%
- **Convolutional Neural Network (CNN)**: Accuracy - 92%
- **Long Short-Term Memory (LSTM)**: Accuracy - 94.6%

## 🛠️ Getting Started
### Prerequisites
- Android Studio installed on your machine.
- A physical Android device (recommended) or an emulator with accelerometer and gyroscope support.

### Installation
1. **Clone this repository**:
   ```bash
   git clone https://github.com/yourusername/FallDetectionActivityPredictionApp.git
