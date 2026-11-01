# Janu AI — প্রাথমিক সংস্করণ

এটি Samsung Galaxy M02 (2 GB RAM)-এর জন্য হালকা Kotlin Android starter project।

## এই সংস্করণে যা কাজ করবে
- অ্যাপ খুলে মাইক্রোফোন বাটনে চাপ দিয়ে বাংলা (`bn-BD`) কথা বলা
- “ইউটিউব” বললে YouTube খোলা
- “ফেসবুক” বললে Facebook খোলা
- “সময়/সময়” বা “কটা বাজে” বললে সময় বলা
- Android-এর Text-to-Speech দিয়ে বাংলায় উত্তর দেওয়ার চেষ্টা

## এখনো তৈরি হয়নি
- অ্যাপ খোলা থাকা অবস্থায় “জানু” wake-word শুনে নিজে থেকে জেগে ওঠা
- স্ক্রিন বন্ধ অবস্থায় wake-word (এই প্রকল্পে লক্ষ্য নয়)
- অনলাইন AI কথোপকথন
- মেয়েলি কণ্ঠ নিশ্চিত করা: ফোনে ইনস্টল থাকা Bengali TTS voice-এর ওপর কণ্ঠ নির্ভর করবে

## APK বানানোর সহজ পথ (কম্পিউটার ছাড়াই)
1. GitHub-এ নতুন repository তৈরি করে এই ZIP-এর ফাইলগুলো আপলোড করুন।
2. Repository-র Actions ট্যাবে যান।
3. “Build Janu AI APK” workflow চালান (অথবা `main` branch-এ push করুন)।
4. কাজ শেষ হলে workflow run-এর Artifacts থেকে `JanuAI-debug-apk` ডাউনলোড করুন।
5. ZIP খুলে `app-debug.apk` ফোনে ইনস্টল করুন। Android চাইলে এই উৎস থেকে install করার অনুমতি দিন।

প্রথমবার অ্যাপ চালু করলে Microphone permission দিন। বাংলা speech recognition-এর জন্য ফোনে Google speech service/ইন্টারনেট প্রয়োজন হতে পারে।
