import { useForm } from "react-hook-form";
import { useNavigate, useSearchParams } from "react-router-dom";
import resetPassword from "../services/auth/resetPassword";
import StyledInput from "../Components/StyledInput";
import FormFieldError from "../Components/FormFieldError";
import { useMutation } from "@tanstack/react-query";
import toast from "react-hot-toast";

function ResetPassPage() {
  const {
    watch,
    register,
    reset,
    handleSubmit,
    formState: { errors },
  } = useForm();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const { mutate } = useMutation({
    mutationFn: (data) => resetPassword(data),
    onSuccess: () => {
      toast.success("Password has been changed");
      reset();
      navigate("/");
    },
    onError: (err) => {
      toast.error(err.message);
      reset();
    },
  });

  function onSubmit(formData) {
    const token = searchParams.get("token");
    if (!token) {
      toast.error(
        "Error: please check that your password reset link is correct."
      );
      return;
    }
    mutate({ token, newPassword: formData.password });
  }

  return (
    <div className="w-full min-h-[82vh] flex flex-col items-center justify-center">
      <form
        className="w-5/6 bg-slate-50 px-3 py-5 flex flex-col space-y-3 lg:w-1/3"
        onSubmit={handleSubmit(onSubmit)}
      >
        <section className="form-field">
          <label>New password</label>
          <input
            type="password"
            id="new-password"
            className="form-field__input"
            {...register("password", {
              required: "Password is required",
              minLength: {
                value: 8,
                message: "Password must be at least 8 symbols long",
              },
            })}
          />
          {errors.password && (
            <FormFieldError>{errors.password.message}</FormFieldError>
          )}
        </section>
        <section className="form-field">
          <label>Confirm new password</label>
          <input
            type="password"
            id="confirm-new-password"
            className="form-field__input"
            {...register("passwordConfirm", {
              required: "Password confirmation is required",
              minLength: {
                value: 8,
                message: "Password must be at least 8 symbols long",
              },
              validate: (value) => {
                if (watch("password") != value) {
                  return "Passwords must match";
                }
              },
            })}
          />
          {errors.passwordConfirm && (
            <FormFieldError>{errors.passwordConfirm.message}</FormFieldError>
          )}
        </section>
        <StyledInput id="reset-pass-submit" type="submit" value="Submit" />
      </form>
    </div>
  );
}

export default ResetPassPage;
