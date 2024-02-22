import { useForm } from "react-hook-form";
import forgotPassword from "../services/auth/forgotPassword";
import { useMutation } from "@tanstack/react-query";
import toast from "react-hot-toast";
import validateEmail from "../utils/validateEmail";
import StyledInput from "../Components/StyledInput";
import FormFieldError from "../Components/FormFieldError";

function ForgotPassPage() {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const { mutate } = useMutation({
    mutationFn: (data) => forgotPassword(data.email),
    onSuccess: () => toast.success("Password reset email sent successfully"),
    onError: (err) => toast.error(err.message),
  });

  function onSubmit(formData) {
    mutate(formData);
  }

  return (
    <div className="w-full min-h-[82vh] flex flex-col justify-center items-center">
      <div className="mx-2 px-2 py-4 border-2 rounded-sm border-slate-100 bg-slate-50 lg:w-1/3">
        <form onSubmit={handleSubmit(onSubmit)}>
          <h2 className="text text-lg font-semibold">Forgot your password?</h2>
          <p>
            You will receive an email with a link and instructions on how to
            reset your password. Please keep in mind that your password reset
            link will be valid for only 10 minutes.
          </p>
          <section className="flex flex-col space-y-2 mt-3">
            <input
              className="px-2 py-1 rounded-sm"
              id="recovery-email"
              placeholder="Email"
              {...register("email", {
                required: "Email is required",
                validate: (val) =>
                  validateEmail(val) || "Email must be of a valid format",
              })}
            />
            {errors.email && (
              <FormFieldError>{errors.email.message}</FormFieldError>
            )}
            <StyledInput id="submit-email" type="submit" value="Submit" />
          </section>
        </form>
      </div>
    </div>
  );
}

export default ForgotPassPage;
