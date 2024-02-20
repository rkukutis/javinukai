import { useState } from "react";
import { useForm } from "react-hook-form";
import registerUser from "../services/auth/registerUser";
import FormFieldError from "../Components/FormFieldError";
import toast from "react-hot-toast";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import StyledInput from "../Components/StyledInput";
import validateEmail from "../utils/validateEmail";

function RegisterPage() {
  const [affiliation, setAffiliation] = useState("freelance");
  const navigate = useNavigate();
  const {
    reset,
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm({ mode: "onBlur" });

  const { mutate } = useMutation({
    mutationFn: (data) => registerUser(data),
    onSuccess: () => {
      toast.success("Registered successfully. Please confirm your email");
      reset();
      navigate("/");
    },
    onError: (err) => {
      toast.error(err.message);
      reset();
    },
  });

  function onSubmit(formData) {
    mutate(formData);
  }

  function onAffiliationChange(newAffiliation) {
    setAffiliation(newAffiliation);
    setValue("institution", undefined);
  }

  return (
    <div className="w-full min-h-[82vh] flex flex-col items-center justify-center">
      <div className="p-3 w-full lg:w-2/5">
        <form
          id="login-form"
          className="flex flex-col space-y-2 bg-slate-50 p-2 rounded-sm"
          onSubmit={handleSubmit(onSubmit)}
        >
          <section className="form-field">
            <label>Name</label>
            <input
              id="name"
              className="form-field__input"
              {...register("name", {
                required: { value: true, message: "Name is required" },
                pattern: {
                  value: /^\S*$/,
                  message: "Name must contain only alphabetic characters",
                },
                maxLength: {
                  value: 20,
                  message: "Name must not exceed 20 characters",
                },
              })}
            />
            {errors.name && (
              <FormFieldError>{errors.name.message}</FormFieldError>
            )}
          </section>
          <section className="form-field">
            <label>Surname</label>
            <input
              id="surname"
              className="form-field__input"
              {...register("surname", {
                required: { value: true, message: "Surname is required" },
                pattern: {
                  value: /^\S*$/,
                  message: "Surname must contain only alphabetic characters",
                },
                maxLength: {
                  value: 20,
                  message: "Surname must not exceed 20 characters",
                },
              })}
            />
            {errors.surname && (
              <FormFieldError>{errors.surname.message}</FormFieldError>
            )}
          </section>
          <section className="form-field">
            <label>Birth Year</label>
            <input
              id="birth-year"
              className="form-field__input"
              {...register("birthYear", {
                required: { value: true, message: "Birth year is required" },
                pattern: {
                  value: /^\d{4}$/,
                  message: "Date must be valid. A valid example: '1984'",
                },
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
            {errors.birthYear && (
              <FormFieldError>{errors.birthYear.message}</FormFieldError>
            )}
          </section>
          <section className="form-field">
            <label>Phone number</label>
            <input
              id="phone-number"
              className="form-field__input"
              {...register("phoneNumber", {
                required: { value: true, message: "Phone number is required" },
                pattern: {
                  value: /^\+?\d{9,11}$/,
                  message: "Phone number must be valid",
                },
                maxLength: {
                  value: 16,
                  message: "Phone number must not exceed 15 numbers",
                },
              })}
            />
            {errors.phoneNumber && (
              <FormFieldError>{errors.phoneNumber.message}</FormFieldError>
            )}
          </section>
          <section className="form-field">
            <label>Email</label>
            <input
              className="form-field__input"
              {...register("email", {
                required: { value: true, message: "Email is required" },
                validate: (val) =>
                  validateEmail(val) || "Email must be of a valid format",
                maxLength: {
                  value: 200,
                  message: "Email must not exceed 200 characters",
                },
              })}
            />
            {errors.email && (
              <FormFieldError>{errors.email.message}</FormFieldError>
            )}
          </section>
          <section className="form-field">
            <label>Password</label>
            <input
              type="password"
              id="password"
              className="form-field__input"
              {...register("password", {
                required: { value: true, message: "Password is required" },
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
            <label>Confirm Password</label>
            <input
              type="password"
              id="password-confirm"
              className="form-field__input"
              {...register("passwordConfirm", {
                required: "Password confirmation is required",
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
          <section className="form-field space-y-2">
            <select
              className="py-2 rounded-sm border-2 border-slate-100"
              id="affiliation-select"
              value={affiliation}
              onChange={(e) => onAffiliationChange(e.target.value)}
            >
              <option value="freelance">Laisvai samdomas</option>
              <option value="institution">
                Atstovaujama žiniasklaidos priemonė
              </option>
            </select>
            {affiliation === "institution" && (
              <div className="form-field">
                <label>Atstovaujamos priemonės pavadinimas</label>
                <input
                  id="institution"
                  className="form-field__input"
                  {...register("institution", {
                    required: "Institution name is required",
                    maxLength: {
                      value: 50,
                      message: "Institution name must not exceed 50 characters",
                    },
                  })}
                />
                {errors.institution && (
                  <FormFieldError>{errors.institution.message}</FormFieldError>
                )}
              </div>
            )}
          </section>
          <StyledInput
            id="registration-submit"
            value="Register"
            type="submit"
          />
        </form>
      </div>
    </div>
  );
}

export default RegisterPage;
