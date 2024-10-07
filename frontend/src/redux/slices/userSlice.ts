import { createSlice, PayloadAction } from '@reduxjs/toolkit';

 interface UserState {
  id: number | null;
  nickname: string | '';
  description: string | '';
  characterName: string | '';
  tierPoint: number | 0;
  mileage: number | 0;
}

const initialState: UserState = {
  id: null,
  nickname: '',
  description: '',
  characterName: '',
  tierPoint: 0,
  mileage: 0,
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
    setUserTierPoint(state, action: PayloadAction<number>) {
      state.tierPoint = action.payload;
    },
    setUserMileage(state, action: PayloadAction<number>) {
      state.mileage = action.payload;
    },
  },
});

export const {
  setUserId,
  clearUserId,
  setUserNickname,
  setUserDescription,
  setUserCharacterName,
  setUserTierPoint,
  setUserMileage,
} = userSlice.actions;
export default userSlice.reducer;
