import { useCallback, useState } from "react";
import { useDropzone } from "react-dropzone";
import uploadImages from "../services/uploadImages";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { useForm } from "react-hook-form";
import StyledInput from "./StyledInput";
import FormFieldError from "./FormFieldError";
import { useTranslation } from "react-i18next";
import { getPhotoUploadMultipart } from "../utils/getMultipartForm";

function ImageUpload({
  userRecord,
  categoryInfo,
  contestInfo,
  setEntryFormOpen,
}) {
  const { t } = useTranslation();
  const [files, setFiles] = useState([]);
  const {
    register,
    reset,
    handleSubmit,
    formState: { errors },
  } = useForm();
  const queryClient = useQueryClient();

  const { mutate, isPending } = useMutation({
    mutationFn: (multipartFormData) => uploadImages(multipartFormData),
    onSuccess: () => {
      toast.success(t("imageUpload.imagesUploaded"));
      setFiles([]);
      queryClient.invalidateQueries(["contestRecord"]);
      setEntryFormOpen(false);
      reset();
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });

  const onDrop = useCallback(
    (acceptedFiles) => {
      if (acceptedFiles.length !== 1 && categoryInfo.type === "SINGLE") {
        toast.error(t("imageUpload.PhotoNumberError"));
        return;
      }
      setFiles(acceptedFiles);
    },
    [categoryInfo.type, t]
  );

  function onSubmit(formData) {
    const { title, description } = formData;
    if (files.length === 0) {
      toast.error(t("imageUpload.imagesAdd"));
      return;
    }
    if (userRecord.entries.length + 1 > userRecord.maxPhotos) {
      toast.error(t("imageUpload.entryLimitError"));
      return;
    }
    mutate({
      data: getPhotoUploadMultipart(
        title,
        description,
        contestInfo.id,
        categoryInfo.id,
        files
      ),
      t,
    });
  }

  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

  return (
    <div className="w-full bg-white shadow rounded px-3 py-5">
      <form onSubmit={handleSubmit(onSubmit)}>
        <section className="form-field">
          <label>{t("imageUpload.photoTitle")}</label>
          <input
            className="form-field__input"
            {...register("title", {
              maxLength: {
                value: 100,
                message: t("imageUpload.titleLength"),
              },
            })}
          />
          {errors.title && (
            <FormFieldError>{errors.title.message}</FormFieldError>
          )}
        </section>
        <section className="form-field">
          <label>
            {t("imageUpload.photoDescription")}
            <span className="text-red-600">*</span>
          </label>
          <textarea
            className="form-field__input"
            {...register("description", {
              required: t("imageUpload.photoDescriptionRequired"),
              maxLength: {
                value: 1000,
                message: t("imageUpload.descriptionLength"),
              },
            })}
          />
          {errors.description && (
            <FormFieldError>{errors.description.message}</FormFieldError>
          )}
        </section>
        <section>
          <ul className="list-disc">
            <p>{t("imageUpload.photoRequirements")}</p>
            <div className="pl-5">
              <li>{t("imageUpload.fileTypeRequirement")}</li>
              <li>{t("imageUpload.photoDimensions")}</li>
              <li>{t("imageUpload.photoSize")}</li>
              <li>{t("imageUpload.photoAllSize")}</li>
              <li>{t("imageUpload.photoAllAtOnce")}</li>
            </div>
          </ul>
          <div
            {...getRootProps()}
            className="text bg-blue-100 h-32 border-2 border-blue-300 border-dotted rounded my-2 hover:border-dashed hover:cursor-pointer flex flex-col justify-center items-center"
          >
            <input {...getInputProps()} />
            {isDragActive ? (
              <p>{t("imageUpload.photoSelectorActive")}</p>
            ) : (
              <p>{t("imageUpload.photoSelector")}</p>
            )}
          </div>
          {files.map((file) => (
            <p key={file.name}>{file.name}</p>
          ))}
        </section>
        <StyledInput
          extraStyle="w-full mt-2"
          disabled={isPending}
          id="image-upload-form"
          type="submit"
          value={
            isPending
              ? t("imageUpload.photoUploading")
              : t("imageUpload.photoUpload")
          }
        />
      </form>
    </div>
  );
}

export default ImageUpload;
