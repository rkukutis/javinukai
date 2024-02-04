import { useState } from "react";
import { useForm } from "react-hook-form";
import registerUser from "../services/registerUser";

function RegisterPage() {
  const [affiliation, setAffiliation] = useState("freelance");
  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm();

  function onSubmit(data) {
    registerUser(data);
  }

  function onAffiliationChange(newAffiliation) {
    setAffiliation(newAffiliation);
    setValue("institution", undefined);
  }

  return (
    <div className="bg-slate-400">
      <form onSubmit={handleSubmit(onSubmit)}>
        <section>
          <label>Name</label>
          <input
            {...register("name", {
              required: { value: true, message: "Name is required" },
              maxLength: {
                value: 20,
                message: "Name must not exceed 20 characters",
              },
            })}
          />
          {errors.name && <p>{errors.name.message}</p>}
        </section>
        <section>
          <label>Surname</label>
          <input
            {...register("surname", {
              required: { value: true, message: "Surname is required" },
              maxLength: {
                value: 20,
                message: "Surname must not exceed 20 characters",
              },
            })}
          />
          {errors.surname && <p>{errors.surname.message}</p>}
        </section>
        <section>
          <label>Birth Year</label>
          <input
            {...register("birthYear", {
              required: { value: true, message: "Birth year is required" },
              max: {
                value: new Date().getFullYear(),
                message: `Birth year can not be later than ${new Date().getFullYear()}`,
              },
              min: {
                value: 1900,
                message: "Birth year can not be earlier than 1900",
              },
            })}
          />
          {errors.birthYear && <p>{errors.birthYear.message}</p>}
        </section>
        <section>
          <label>Phone number</label>
          <input
            {...register("phoneNumber", {
              required: { value: true, message: "Phone number is required" },
              maxLength: {
                value: 16,
                message: "Phone number must not exceed 15 numbers",
              },
            })}
          />
          {errors.phoneNumber && <p>{errors.phoneNumber.message}</p>}
        </section>
        <section>
          <label>Email</label>
          <input
            {...register("email", {
              required: { value: true, message: "Email is required" },
              validate: (val) =>
                val.match(
                  "/^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,})$/"
                ) == null || "Email must be of a valid format",
              maxLength: {
                value: 200,
                message: "Email must not exceed 200 characters",
              },
            })}
          />
          {errors.email && <p>{errors.email.message}</p>}
        </section>
        <section>
          <label>Password</label>
          <input
            {...register("password", {
              required: { value: true, message: "Password is required" },
              minLength: {
                value: 8,
                message: "Password must be at least 8 symbols long",
              },
            })}
          />
          {errors.password && <p>{errors.password.message}</p>}
        </section>
        <section>
          <label>Confirm Password</label>
          <input
            {...register("passwordConfirm", {
              required: true,
              validate: (value) => {
                if (watch("password") != value) {
                  return "Passwords must match";
                }
              },
            })}
          />
          {errors.passwordConfirm && <p>{errors.passwordConfirm.message}</p>}
        </section>
        <section>
          <select
            value={affiliation}
            onChange={(e) => onAffiliationChange(e.target.value)}
          >
            <option value="freelance">Laivai samdomas</option>
            <option value="institution">
              Atstovaujama žiniasklaidos priemonė
            </option>
          </select>
          {affiliation === "institution" && (
            <div>
              <label>Atstovaujamos priemonės pavadinimas</label>
              <input
                {...register("institution", {
                  required: false,
                  maxLength: {
                    value: 50,
                    message: "Institution name must not exceed 50 characters",
                  },
                })}
              />
            </div>
          )}
        </section>
        <input type="submit" value="Register" />
      </form>
    </div>
  );
}

export default RegisterPage;
