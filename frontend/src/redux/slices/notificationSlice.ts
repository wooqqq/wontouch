import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface NotificationState {
  count: number | 0;
}

const initialState: NotificationState = {
  count: 0,
};

const notificationSlice = createSlice({
  name: 'notification',
  initialState,
  reducers: {
    updateNotificationCount(state) {
      state.count += 1;
    },
    setNotificationCount(state, action: PayloadAction<number>) {
      state.count = action.payload;
    },
  },
});

export const { updateNotificationCount, setNotificationCount } =
  notificationSlice.actions;
export default notificationSlice.reducer;
