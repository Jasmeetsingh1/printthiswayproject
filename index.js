var express = require("express")
var bodyParser = require("body-parser")
var mongoose = require("mongoose")
var multer = require("multer")
var path = require("path")
const app = express()
app.use(bodyParser.json())
app.use(express.static('public'))
app.use(bodyParser.urlencoded({
    extended:true
}))

mongoose.connect('mongodb://localhost:27017/taxDB',{
    useNewUrlParser: true,
    useUnifiedTopology: true
});


var db = mongoose.connection;

db.on('error',()=>console.log("Error in Connecting to Database"));
db.once('open',()=>console.log("Connected to Database"))
//multer for file upload
var storage = multer.diskStorage({
    destination: function(req, file, cb) {
        cb(null, 'uploads/') // Upload files to the 'uploads' directory
    },
    filename: function(req, file, cb) {
        cb(null, file.fieldname + '-' + Date.now() + path.extname(file.originalname)) // Append timestamp to file name
    }
});

var upload = multer({
    storage: storage
});

// Define a mongoose schema for file metadata
var fileSchema = new mongoose.Schema({
    filename: String,
    path: String,
    contentType: String,
    size: Number,
    uploadDate: {
        type: Date,
        default: Date.now
    }
});

var File = mongoose.model('File', fileSchema);

// Handle file upload
app.post("/upload", upload.single('file'), function(req, res) {
    try {
        // Create a new File document
        var newFile = new File({
            filename: req.file.originalname,
            path: req.file.path,
            contentType: req.file.mimetype,
            size: req.file.size
        });

        // Save the File document to MongoDB
        newFile.save(function (err, savedFile) {
            if (err) {
                console.error(err);
                res.status(500).send('Error uploading file');
            } else {
                console.log('File uploaded successfully:', savedFile);
                res.send('File uploaded successfully!');
            }
        });
    } catch (err) {
        console.error(err);
        res.status(500).send('Error uploading file');
    }

    db.collection('files').insertOne(newFile,(err,collection)=>{
        if(err){
            throw err;
        }
       console.log("Record Inserted Successfully");
    });

    //return res.redirect('main4.html')
});
//signup
app.post("/sign_up",(req,res)=>{
    var firstName = req.body.firstName;
    var lastName = req.body.lastName;
    var email = req.body.email;
    var password = req.body.password;

    var data = {
        "firstName": firstName,
        "lastName" : lastName,
        "email" : email,
        "password" : password
    }

    db.collection('users').insertOne(data,(err,collection)=>{
        if(err){
            throw err;
        }
        console.log("Record Inserted Successfully");
    });

    return res.redirect('main4.html')

})

app.post("/login", async (req, res) => {
    try {
        const check = await db.collection('users').findOne({ firstName: req.body.username});
        if (check.password === req.body.password) {
            res.redirect('main4.html')
        }
        else {
            res.send("wrong Password");
        }
    }
    catch {
        res.send("wrong Details");
    }
});
app.post("/form-submit", function(req, res) {
    // Handle form submission data here if needed
    // Redirect back to the same page
    res.redirect('/dropdown.html');
});


app.get("/",(req,res)=>{
    res.set({
        "Allow-access-Allow-Origin": '*'
    })
    return res.redirect('main4.html');
}).listen(3000);


console.log("Listening on PORT 3000");
