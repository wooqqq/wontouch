import { configureStore } from '@reduxjs/toolkit';
import userSlice from './slices/userSlice';
import authSlice from './slices/authSlice';
import roomSlice from './slices/roomSlice';

export const store = configureStore({
  reducer: {
    auth: authSlice,
    user: userSlice,
    room: roomSlice,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
