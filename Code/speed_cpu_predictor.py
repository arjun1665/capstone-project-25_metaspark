# speed_cpu_predictor.py
import numpy as np
import tensorflow as tf
from sklearn.preprocessing import MinMaxScaler
from tensorflow.keras.models import Sequential, load_model
from tensorflow.keras.layers import LSTM, Dense, Dropout

class SpeedCPU_Predictor:
    def __init__(self, time_steps):
        self.time_steps = time_steps
        self.num_features = 2 
        self.scaler = MinMaxScaler(feature_range=(0, 1))
        self.model = self._create_lstm_model()
        self.is_trained = False

    def _create_lstm_model(self):
        model = Sequential()
        model.add(LSTM(units=128, input_shape=(self.time_steps, self.num_features), return_sequences=True))
        model.add(Dropout(0.2))
        model.add(LSTM(units=64))
        model.add(Dropout(0.2))
        model.add(Dense(units=self.num_features, activation='linear'))
        model.compile(optimizer='adam', loss='mean_squared_error')
        return model

    def train(self, data, epochs=100, batch_size=1):
        if len(data) < self.time_steps + 1:
            return False
        scaled_data = self.scaler.fit_transform(data)
        X, y = self._create_dataset(scaled_data)
        y = y.reshape(y.shape[0], self.num_features)
        self.model.fit(X, y, epochs=epochs, batch_size=batch_size, verbose=0)
        self.is_trained = True
        return True

    def _create_dataset(self, data):
        X, y = [], []
        for i in range(len(data) - self.time_steps):
            X.append(data[i:(i + self.time_steps)])
            y.append(data[i + self.time_steps])
        return np.array(X), np.array(y)

    def predict_next(self, last_known_data, num_predictions=1):
        if not self.is_trained:
            return []
        predictions = []
        current_input_sequence = self.scaler.transform(last_known_data).reshape(1, self.time_steps, self.num_features)
        for _ in range(num_predictions):
            predicted_scaled = self.model.predict(current_input_sequence, verbose=0)
            predicted_values = self.scaler.inverse_transform(predicted_scaled)
            predictions.append(predicted_values[0])
            current_input_sequence = np.vstack([current_input_sequence[0][1:], predicted_scaled[0].reshape(1, -1)]).reshape(1, self.time_steps, self.num_features)
        return predictions