import { Outlet } from "react-router-dom";
import { Provider } from "../components/ui/provider";

export default function AuthLayout() {
  return (
    <Provider>
      <Outlet />
    </Provider>
  );
}
