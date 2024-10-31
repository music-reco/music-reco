import { Outlet } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { useMemo } from "react";
import { Provider } from "../components/ui/provider";
import { Box } from "@chakra-ui/react";
// import { Toaster } from "@/components/ui/toaster";

export default function MainLayout() {
  const queryClient = useMemo(() => new QueryClient(), []);
  return (
    <Provider>
      <QueryClientProvider client={queryClient}>
        <Box bg="#02001F" minHeight="100vh" width="100vw">
          {/* <Toaster /> */}
          <Outlet />
        </Box>
      </QueryClientProvider>
    </Provider>
  );
}
