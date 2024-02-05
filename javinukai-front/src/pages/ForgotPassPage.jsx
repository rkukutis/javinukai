import { useForm } from "react-hook-form";
import forgotPassword from "../services/forgotPassword";
import { useMutation } from "@tanstack/react-query";
import toast from "react-hot-toast";

function ForgotPassPage() {
  const { register, handleSubmit } = useForm();

  const { mutate } = useMutation({
    mutationFn: (data) => forgotPassword(data.email),
    onSuccess: () => toast.success("Password reset email sent successfully"),
    onError: () => toast.success("Password reset email sent successfully"),
    // both send the same message to avoid probing attacks
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
              {...register("email")}
            />
            <input
              type="submit"
              value="Submit"
              className="w-full bg-blue-500 rounded-sm py-1 text-slate-50"
            />
          </section>
        </form>
      </div>
    </div>
  );
}

export default ForgotPassPage;
