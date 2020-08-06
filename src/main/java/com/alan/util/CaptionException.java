class CaptionException extends Exception {
    @Override
    public void printStackTrace() {
        System.out.print("my Exception: ");
        super.printStackTrace();
    }
}