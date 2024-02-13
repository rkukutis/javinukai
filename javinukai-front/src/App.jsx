import { BrowserRouter, Route, Routes } from "react-router-dom";
import Layout from "./Components/Layout";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import { Toaster } from "react-hot-toast";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import ForgotPassPage from "./pages/ForgotPassPage";
import ResetPassPage from "./pages/ResetPassPage";
import ConfirmEmailPage from "./pages/ConfirmEmailPage";
import HomePage from "./pages/HomePage";


const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
    },
  },
});

function App() {
  return (
    <>
      <QueryClientProvider client={queryClient}>
        <Toaster
          position="top-center"
          gutter={12}
          containerStyle={{ margin: "8px" }}
          toastOptions={{
            success: { duration: 3000 },
            error: { duration: 5000 },
            style: {
              fontSize: "16px",
              maxWidth: "500px",
              padding: "16px 24px",
            },
          }}
        />
        <BrowserRouter>
          <Routes>

            <Route path="/" element={<Layout />}>
              <Route index element={<HomePage />}/>
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />
              <Route path="/forgot-password" element={<ForgotPassPage />} />
              <Route path="/reset-password" element={<ResetPassPage />} />
              <Route path="/confirm-email" element={<ConfirmEmailPage />} />
              
            </Route>
          </Routes>
        </BrowserRouter>
        <ReactQueryDevtools initialIsOpen={false} />
      </QueryClientProvider>
      
    </>
  );
}

export default App;
