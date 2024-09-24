// src/redux/slices/gameSlice.ts
import { createSlice } from "@reduxjs/toolkit";

interface GameState {
  score: number;
}

const initialState: GameState = {
  score: 0,
};

const gameSlice = createSlice({
  name: "game",
  initialState,
  reducers: {
    incrementScore: (state) => {
      state.score += 1;
    },
    resetScore: (state) => {
      state.score = 0;
    },
  },
});

export const { incrementScore, resetScore } = gameSlice.actions;

export default gameSlice.reducer;
