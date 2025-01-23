public interface ILiberian {

    void registerMember( int studentID,  String firstname, String lastname, String email,
                         boolean registered, String level, int limitNr, int borrowedNr, int warning, boolean active);
    void UnRegisterMember( int studentID);
    void suspend(int studentID);

}
