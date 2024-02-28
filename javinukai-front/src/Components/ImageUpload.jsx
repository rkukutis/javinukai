import { useCallback, useState } from "react";
import { useDropzone } from "react-dropzone";
import uploadImages from "../services/uploadImages";
import { useMutation } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { useForm } from "react-hook-form";
import StyledInput from "./StyledInput";
import FormFieldError from "./FormFieldError";
import { useTranslation } from "react-i18next";

function getMultipartFormData(title, description, images) {
  let formData = new FormData();
  formData.append("categoryId", "a201c592-7daf-465e-92ab-6c3f9a45c1a9");
  formData.append("title", title);
  formData.append("description", description);
  images.map((image) => formData.append("image", image));
  return formData;
}

function ImageUpload() {
  const [files, setFiles] = useState([]);
  const [uploadedCollection, setUploadedCollection] = useState();
  const {
    register,
    reset,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const { mutate, isPending } = useMutation({
    mutationFn: (multipartFormData) => uploadImages(multipartFormData),
    onSuccess: (createdCollection) => {
      toast.success(t('imageUpload.imagesUploaded'));
      setFiles([]);
      reset();
      setUploadedCollection(createdCollection);
    },
    onError: () => toast.error(t('services.uploadImagesError')),
  });

  const onDrop = useCallback((acceptedFiles) => {
    setFiles(acceptedFiles);
  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

  function onSubmit(formData) {
    const { title, description } = formData;
    if (files.length === 0) {
      toast.error(t('imageUpload.imagesAdd'));
      return;
    }
    mutate(getMultipartFormData(title, description, files));
  }

  const { t } = useTranslation();

  return (
    <div className="w-full min-h-[82vh] flex flex-col justify-center items-center bg-slate-50">
      <div className="w-full bg-white lg:w-1/3 border-2 px-3 py-5">
        <h1 className="text-2xl text-center">{t('imageUpload.photoUploadTitle')}</h1>
        <form onSubmit={handleSubmit(onSubmit)}>
          <section className="form-field">
            <label>
            {t('imageUpload.photoUpload')}<span className="text-red-600">*</span>
            </label>
            <input
              className="form-field__input"
              {...register("title", {
                required: t('imageUpload.photoTitleRequired'),
              })}
            />
            {errors.title && (
              <FormFieldError>{errors.title.message}</FormFieldError>
            )}
          </section>
          <section className="form-field">
            <label>
              {t('imageUpload.photoDescription')}<span className="text-red-600">*</span>
            </label>
            <textarea
              className="form-field__input"
              {...register("description", {
                required: t('imageUpload.photoDescriptionRequired'),
              })}
            />
            {errors.description && (
              <FormFieldError>{errors.description.message}</FormFieldError>
            )}
          </section>
          <section>
            <ul className="list-disc">
              <p>{t('imageUpload.photoRequirements')}</p>
              <div className="pl-5">
                <li>{t('imageUpload.photoDimensions')}</li>
                <li>{t('imageUpload.photoSize')}</li>
                <li>{t('imageUpload.photoAllSize')}</li>
              </div>
            </ul>
            <div
              {...getRootProps()}
              className="text bg-blue-100 h-16 border-2 border-blue-300 border-dotted my-2 hover:border-dashed flex flex-col justify-center items-center"
            >
              <input {...getInputProps()} />
              {isDragActive ? (
                <p>{t('imageUpload.photoSelectorActive')}</p>
              ) : (
                <p>{t('imageUpload.photoSelector')}</p>
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
            value={isPending ? t('imageUpload.photoUploading') : t('imageUpload.photoUpload')}
          />
        </form>
        {uploadedCollection && (
          <div className="mt-3 border-2 p-3">
            <h1>{t('imageUpload.collectionTitle')} {uploadedCollection?.name}</h1>
            <p>{t('imageUpload.collectionDescription')} {uploadedCollection?.description}</p>
            <p>{t('imageUpload.collectionCreatedAt')} {uploadedCollection?.createdAt}</p>
            {uploadedCollection?.images.map((image) => (
              <img key={image.uuid} src={image.urlThumbnail} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default ImageUpload;
