import { useForm } from "react-hook-form";
import loginUser from "../services/loginUser";

function LoginPage() {
  const { register, handleSubmit } = useForm();

  async function onSubmit(data) {
    console.log(data);
    const res = await loginUser(data);
    console.log(res);
  }

  return (
    <div className="bg-slate-300">
      <form onSubmit={handleSubmit(onSubmit)}>
        <section>
          <label>Email</label>
          <input {...register("email")} />
        </section>
        <section>
          <label>Password</label>
          <input {...register("password")} />
        </section>
        <input type="submit" value="Login" />
      </form>
    </div>
  );
}

export default LoginPage;
