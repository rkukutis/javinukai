import { useForm } from "react-hook-form";
import { Link, useNavigate } from "react-router-dom";
import FormFieldError from "../Components/FormFieldError";
import { useMutation } from "@tanstack/react-query";
import toast from "react-hot-toast";
import useUserStore from "../stores/userStore";
import StyledInput from "../Components/StyledInput";
import validateEmail from "../utils/validateEmail";
import loginUser from "../services/auth/loginUser";

function LoginPage() {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();
  const navigate = useNavigate();

  const setUser = useUserStore((state) => state.setUser);
  const { mutate } = useMutation({
    mutationFn: (data) => loginUser(data),
    onSuccess: (loggedInUser) => {
      toast.success("Logged in successfully");
      setUser(loggedInUser);
      navigate("/");
    },
    onError: (err) => toast.error(err.message),
  });

  const handleLoginSubmit = async (formData) => {
    mutate(formData);
  };

  return (
    <div className="w-full h-[82vh] flex flex-col justify-center items-center">
      <div className="bg-slate-100 border-2 border-slate-200 p-3 w-5/6 lg:w-1/4 md:w-1/2">
        <form
          id="login-form"
          className="flex flex-col space-y-3"
          onSubmit={handleSubmit(handleLoginSubmit)}
        >
          <section className="form-field">
            <label>Email</label>
            <input
              className="form-field__input"
              {...register("email", {
                required: "Email is required",
                validate: (val) =>
                  validateEmail(val) || "Email must be of a valid format",
              })}
            />
            {errors.email && (
              <FormFieldError>{errors.email.message}</FormFieldError>
            )}
          </section>
          <section className="flex flex-col space-y-1">
            <label className="text text-slate-800">Password</label>
            <input
              type="password"
              className="form-field__input"
              {...register("password", { required: "Password is required" })}
            />
            {errors.password && (
              <FormFieldError>{errors.password.message}</FormFieldError>
            )}
            <div className="flex space-x-4 text-blue-500 text-sm pt-2">
              <Link to="/forgot-password">Forgotten password?</Link>
              <Link to="/register">Register</Link>
            </div>
          </section>
          <StyledInput id="login-submit" value="Log In" type="submit" />
        </form>
      </div>
    </div>
  );
}

export default LoginPage;
