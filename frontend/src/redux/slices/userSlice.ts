import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface UserState {
  id: number | null;
  nickname: string | null;
}

const initialState: UserState = {
  id: null,
  nickname: null,
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUserId(state, action: PayloadAction<number>) {
      state.id = action.payload;
    },
    clearUserId(state) {
      state.id = null;
    },
    setUserNickname(state, action: PayloadAction<string>) {
      state.nickname = action.payload;
    },
  },
});

export const { setUserId, clearUserId, setUserNickname } = userSlice.actions;
export default userSlice.reducer;
