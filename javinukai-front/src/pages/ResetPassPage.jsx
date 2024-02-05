import { useForm } from "react-hook-form";
import { useSearchParams } from "react-router-dom";
import resetPassword from "../services/resetPassword";

function ResetPassPage() {
  const { register, handleSubmit } = useForm();
  const [searchParams, setSearchParams] = useSearchParams();
  console.log(searchParams.get("token"));

  function onSubmit(data) {
    const token = searchParams.get("token");
    if (!token) return;
    resetPassword(token, data.password);
  }

  return (
    <div>
      <form onSubmit={handleSubmit(onSubmit)}>
        <section>
          <label>New password</label>
          <input {...register("password")} />
        </section>
        <section>
          <label>Confirm new password</label>
          <input {...register("passwordConfirm")} />
        </section>
        <input type="submit" value="Reset password" />
      </form>
    </div>
  );
}

export default ResetPassPage;
