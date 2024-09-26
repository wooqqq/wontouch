// src/App.tsx
import React from "react";
import { useSelector, useDispatch } from "react-redux";
import { RootState } from "./redux/store";
import AppRouter from "./AppRouter";

const App: React.FC = () => {
  return (
    <div>
      <AppRouter />
    </div>
  );
};

export default App;
