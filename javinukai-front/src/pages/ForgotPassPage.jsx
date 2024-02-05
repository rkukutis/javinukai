import { useForm } from "react-hook-form";
import forgotPassword from "../services/forgotPassword";

function ForgotPassPage() {
  const { register, handleSubmit } = useForm();
  function onSubmit(data) {
    const res = forgotPassword(data.email);
    console.log(res);
  }

  return (
    <div>
      <form onSubmit={handleSubmit(onSubmit)}>
        <section className="text bg-slate-400">
          <label>Email</label>
          <input {...register("email")} />
          <input type="submit" value="reset password" />
        </section>
      </form>
    </div>
  );
}

export default ForgotPassPage;
