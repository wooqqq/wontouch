// src/App.tsx
import React from "react";
import { useSelector, useDispatch } from "react-redux";
import { RootState } from "./redux/store";
import { incrementScore, resetScore } from "./redux/slices/gameSlice";
import AppRouter from "./AppRouter";

const App: React.FC = () => {
  return (
    <div>
      {/* <h1>Score: {score}</h1>
      <button onClick={() => dispatch(incrementScore())}>Increment Score</button>
      <button onClick={() => dispatch(resetScore())}>Reset Score</button> */}
      <AppRouter />
    </div>
  );
};

export default App;
