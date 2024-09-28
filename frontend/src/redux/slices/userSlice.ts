import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface UserState {
  id: number | null;
  nickname: string | null;
  description: string | null;
  characterName: string | null;
}

const initialState: UserState = {
  id: null,
  nickname: null,
  description: null,
  characterName: null,
};

const userSlice = createSlice({
  name: 'user',
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
    setUserDescription(state, action: PayloadAction<string>) {
      state.description = action.payload;
    },
    setUserCharacterName(state, action: PayloadAction<string>) {
      state.characterName = action.payload;
    },
  },
});

export const {
  setUserId,
  clearUserId,
  setUserNickname,
  setUserDescription,
  setUserCharacterName,
} = userSlice.actions;
export default userSlice.reducer;
